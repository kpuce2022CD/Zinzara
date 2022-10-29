import cv2

path = "hand_gesture_data/data5/9/"
i = 1
img = cv2.imread(path + str(i) + ".jpg", cv2.IMREAD_COLOR)

while True:
    img = cv2.resize(img, dsize=(120, 160))
    cv2.imwrite(path + str(i) + ".jpg", img)
    i += 1
    img = cv2.imread(path + str(i) + ".jpg", cv2.IMREAD_COLOR)
