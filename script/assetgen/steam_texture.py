from __future__ import annotations

import math
import random
from PIL import Image

frame_count = 32
frame_scale = 1 / frame_count

class Point:
    def __init__(self, x: float, y: float):
        self.x = x % 1
        self.y = y % 1

    def __add__(self, other: Point) -> Point:
        return Point(self.x + other.x, self.y + other.y)

    def __sub__(self, other: Point) -> Point:
        return self + (-other)

    def __iadd__(self, other: Point):
        self.x += other.x
        self.x %= 1
        self.y += other.y
        self.y %= 1
        return self

    def __neg__(self):
        return Point(-self.x, -self.y)

    def __rmul__(self, other: float):
        return Point(other * self.x, other * self.y)

    def __truediv__(self, other: float):
        return (1 / other) * self

    def __mul__(self, other: float):
        return other * self

    def dist(self, other: Point):
        dx = abs(self.x - other.x)
        dy = abs(self.y - other.y)
        dx = min(dx, 1 - dx)
        dy = min(dy, 1 - dy)
        return math.hypot(dx, dy)

def clamp(v, l, u):
    return max(min(v, u), l)

def colorify(value: float, a_scale: float = 0.5):
    r = int(value * 255)
    g = r
    b_scale = 0.12
    b = int(((1 - b_scale) * value + b_scale) * 255)
    a = int((a_scale + 0.2 * value) * 255)
    return r, g, b, a

def add_layer(point_count, color_func):
    image = Image.new("RGBA", (16, 16 * frame_count))
    initial_points = [Point(random.random(), random.random()) for x in
              range(point_count)]
    vels = []
    for _ in range(len(initial_points)):
        y = -random.randint(1, 3)
        x = round(random.triangular(-3, 3))
        # l = math.hypot(x, y)
        # y = y / l
        # x = x / l
        vels.append((x, y))

    for i in range(frame_count):
        points = [p + Point(i * frame_scale * v[0], i * frame_scale * v[1]) for (p, v) in zip(initial_points, vels)]
        frame = Image.new("RGBA", (16, 16))
        for x in range(16):
            for y in range(16):
                closest = points[0]
                u = Point((x + 0.5) / 16, (y + 0.5) / 16)
                c_dist = closest.dist(u)
                for v in points:
                    d = u.dist(v)
                    if d < c_dist:
                        closest = v
                        c_dist = d
                dy = (u.y - closest.y + 0.5) % 1.0 - 0.5
                value = clamp(c_dist, 0, 1)
                color = color_func(dy, value)
                frame.putpixel((x, y), color)
        image.paste(frame, (0, i * 16))
    return image

def l1color(dy, value):
    y_diff = clamp(dy, -0.2, 0.3)
    value = value ** (1 + y_diff)
    value = 1 - value
    value = clamp(value, 0.7, 1)
    return colorify(value, 0.6)

def l2color(dy, value):
    y_diff = clamp(dy, -0.2, 0.3)
    value = clamp(value ** 0.5 - 0.1, 0, 1)
    value = 1 - value
    r = int(value * 255)
    return r, r, r, 70

def l3color(dy, value):
    y_diff = clamp(dy, -0.2, 0.3)
    value = clamp(value ** 0.3 + 0.2, 0, 1)
    r = int(value * 255)
    return r, r, r, 40

def main():
    seed = 1019
    random.seed(seed)
    point_count = 10
    image = add_layer(point_count, l1color)
    l2 = add_layer(14, l2color)
    l3 = add_layer(36, l3color)
    image.alpha_composite(l2)
    image.alpha_composite(l3)

    image.save("steam.png")


if __name__ == "__main__":
    main()
