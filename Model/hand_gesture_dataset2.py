import cv2
import mediapipe as mp
import math
import os


def find_distance(a1, a2, b1, b2):
    n1 = abs(a1 - b1)
    n2 = abs(a2 - b2)
    return round(math.sqrt((n1**2) + (n2**2)), 4)


def find_gradient(a1, a2, b1, b2):
    n1 = b1-a1
    n2 = b2-a2
    if n1 == 0:
        return 0
    else:
        return round(n2/n1*-1, 4)


def find_angle(a1, a2, b1, b2):
    n1 = b1 - a1
    n2 = b2 - a2
    return round(math.atan2(n1, n2) * 180 / math.pi, 4)


with open("hand_gesture_data/hand_gesture_data1_tmp.csv", "r") as f1:
    with open("qwe.csv", "w") as f2:
        _ = f1.readline()

        while True:
            line = f1.readline()
            line = line.strip("\n")
            if not line:
                break

            line = line.split(",")
            # f2.write(find_angle(line[2], line[3], line[8], line[9]))
            # f2.write(find_angle(line[10], line[11], line[16], line[17]))
            # f2.write(find_angle(line[18], line[19], line[24], line[25]))
            # f2.write(find_angle(line[26], line[27], line[32], line[33]))
            # f2.write(find_angle(line[34], line[35], line[40], line[41]))
            f2.write(str(find_angle(int(line[2]), int(line[3]), int(line[8]), int(line[9]))) + ",")
            f2.write(str(find_angle(int(line[10]), int(line[11]), int(line[16]), int(line[17]))) + ",")
            f2.write(str(find_angle(int(line[18]), int(line[19]), int(line[24]), int(line[25]))) + ",")
            f2.write(str(find_angle(int(line[26]), int(line[27]), int(line[32]), int(line[33]))) + ",")
            f2.write(str(find_angle(int(line[34]), int(line[35]), int(line[40]), int(line[41]))) + "\n")


# with open("hand_gesture_data/hand_gesture_data1_tmp.csv", "r") as f1:
#     print(f1.readline())
#     # print(f1.readline())
#     a = f1.readline()
#     print(a)
#     a = a.strip("\n")
#     a = a.split(",")
#     print(a)
