import tensorflow as tf
import pathlib
import os
import cv2
import pickle

img_path = pathlib.Path("hand_gesture_data/data4/test/")
file_list = sorted([str(path) for path in img_path.glob("*.jpg")])
print(file_list)
print(len(file_list))

# n = 1
#
# for i in file_list:
#     img = cv2.imread(i, cv2.IMREAD_COLOR)
#     name = "hand_gesture_data/data4/ttest/" + "9_" + str(n) + ".jpg"
#     n += 1
#     cv2.imwrite(name, img)

labels = []
for file in file_list:
    if os.path.basename(file)[0] is "0":
        labels.append("0")
    elif os.path.basename(file)[0] is "1":
        labels.append("1")
    elif os.path.basename(file)[0] is "2":
        labels.append("2")
    elif os.path.basename(file)[0] is "3":
        labels.append("3")
    elif os.path.basename(file)[0] is "4":
        labels.append("4")
    elif os.path.basename(file)[0] is "5":
        labels.append("5")
    elif os.path.basename(file)[0] is "6":
        labels.append("6")
    elif os.path.basename(file)[0] is "7":
        labels.append("7")
    elif os.path.basename(file)[0] is "8":
        labels.append("8")
    else:
        labels.append("9")

print(len(labels))

p = open("test_label", "wb")
pickle.dump(labels, p)