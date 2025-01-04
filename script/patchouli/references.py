import re
from collections.abc import Callable

root = "../../src/main/"
java_root = "java/"
file_root = "resources/assets/factoryautomation/patchouli_books/guidebook/en_us/entries/references/"

minusOne = "\\u207B\\u00B9"
dot = "\\u00B7"
squared = "\\u00B3"


def init_table(file: str, constTable: dict[str, str] = {}) -> dict[str, dict[str, any]]:
    java_file = open(root + java_root + file)
    className = ""
    callCache: dict[str, list[str]] = {}
    lines = java_file.readlines()
    for line in lines:
        if "class" in line:
            cI = line.split(" ").index("class")
            className = line.split(" ")[cI + 1].strip()
            pattern = re.compile(rf"new {className}\((.+)\);")
            constructorPattern = re.compile(rf"{className}\((.+)\)")
        elif "new " + className in line:
            line = line.split(" = ")
            identifier = line[0].split(" ")
            identifier = identifier[identifier.index(className) + 1]
            identifier = identifier.lower()
            args = line[1]
            args = pattern.search(args).group(1)
            args = map(str.strip, args.split(","))
            callCache[identifier] = list(args)
        elif className + "(" in line:
            conArgs = constructorPattern.search(line).group(1).split(",")
            conArgs = map(str.strip, conArgs)
            constructorMapper: list[tuple[str, Callable[[str], any]]] = []
            for arg in conArgs:
                arg = arg.split(" ")
                type = arg[0]
                if type == "String":
                    constructorMapper.append((arg[-1], lambda x: x.strip("\"")))
                elif type == "float":
                    constructorMapper.append((arg[-1], lambda x: eval(x)))
                elif type == "int":
                    constructorMapper.append((arg[-1], lambda x: eval(x)))
                else:
                    constructorMapper.append((arg[-1], lambda _: None))

    def constructor(args: list[str]) -> dict[str, any]:
        d: dict[str, any] = {}
        for t in zip(constructorMapper, args):
            param = t[1]
            for c, v in constTable.items():
                param = param.replace(c, v)
            r = t[0][1](param)
            if r is not None:
                d[t[0][0]] = r
        return d

    table: dict[str, dict[str, any]] = {}

    for name, args in callCache.items():
        table[name] = constructor(args)

    return table


def generateSection(fileName: str, name: str, icon: str, desc: str, props: list[str], propFormatters: list[Callable[[any], str]], propNames: list[str], table: dict[str, dict[str, any]], itemGetter: Callable[[str], str], excluded: set[str]) -> None:
    realDesc = f"{desc}$(br)"
    for prop, propN in zip(props, propNames):
        realDesc += f"$(li)$(l:references/{fileName[:-5]}#{prop}){propN}"
    introstring = f"""{{
\t"name": "{name}",
\t"icon": "{icon}",
\t"category": "factoryautomation:references",
\t"sortnum": 0,
\t"pages": [
\t\t{{
\t\t\t"type": "text",
\t\t\t"text": "{realDesc}"
\t\t}},
\t\t{{
\t\t\t"type": "empty"
\t\t}}"""
    startstring = lambda n: f""",
\t\t{{
\t\t\t"type": "factoryautomation:dict_{n}","""
    joinstring = """
\t\t}"""
    endstring = """
\t\t}"""
    finalstring = """
\t]
}"""

    keys = [k for k in table if k not in excluded]

    file = open(root + file_root + fileName, 'w')
    file.write(introstring)

    def generatePage(prop: str, propF: Callable[[any], str], propN: str, vals: list[str]) -> None:
        file.write(startstring(len(vals)))
        file.write(f'\n\t\t\t"title": "{propN}"')
        file.write(f',\n\t\t\t"anchor": "{prop}"')
        for i, val in enumerate(vals):
            file.write(f',\n\t\t\t"i{i + 1}": "{itemGetter(val)}"')
            file.write(f',\n\t\t\t"t{i + 1}": "{propF(table[val][prop])}"')
        file.write(endstring)

    for prop, propF, propN in zip(props, propFormatters, propNames):
        for i in range(len(keys) // 8 + 1):
            vals = keys[i * 8 : (i + 1) * 8]
            generatePage(prop, propF, propN if i == 0 else "cont.", vals)
    file.write(finalstring)
    file.close()


def generate_all():
    metalTable = init_table("boblovespi/factoryautomation/common/util/Metal.java")
    print(metalTable)
    generateSection("0metals.json", "Properties of Metals", "minecraft:iron_ingot", "A list of the properties of metals, sorted by property.",
                    ["meltTemp", "density", "massHeatCapacity"],
                    [lambda f: f"{f+273:,.0f} K", lambda f: f"{f:,.0f} kg/m{squared}", lambda f: f"{f:,.0f} J{dot}kg{minusOne}K{minusOne}"],
                    ["Melting Points", "Densities", "Mass Heat Capacities"],
                    metalTable, lambda i: f'{"minecraft" if i in {"iron", "copper", "gold"} else "factoryautomation"}:{i}_ingot', {"unknown"})

    formTable = init_table("boblovespi/factoryautomation/common/util/Form.java",
                           {"Metal.UNITS_IN_INGOT": "18", "Integer.MAX_VALUE": "0", "/": "//"})
    formTypes = {"stone": 1.5, "brick": 4 / 3}
    formTable = {k: {k2: v["amount"] if k in {"ingot", "nugget"} else int(v2 * v["amount"]) for k2, v2 in formTypes.items()} for k, v in formTable.items() if k in {"ingot", "nugget", "sheet", "rod", "gear"}}
    print(formTable)
    generateSection("1forms.json", "Forms of Metals", "factoryautomation:iron_rod", "The different forms of metals, along with units per cast for each casting vessel.",
                    ["stone", "brick"],
                    [lambda v: f"{v} units", lambda v: f"{v} units"],
                    ["Units per Cast (Stone)", "Units per Cast (Brick)"],
                    formTable, lambda i: f'{"minecraft" if i in {"ingot", "nugget", "gold"} else "factoryautomation"}:iron_{i}', set())


if __name__ == "__main__":
    generate_all()
