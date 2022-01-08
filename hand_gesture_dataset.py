import cv2
import mediapipe as mp
import math
import csv

mp_drawing=mp.solutions.drawing_utils
mp_hands=mp.solutions.hands

cap=cv2.VideoCapture(0)

f=open("test.csv","a")

def find_distance(a1,a2,b1,b2):
    n1=abs(a1-b1)
    n2=abs(a2-b2)
    return math.sqrt((n1**2)+(n2**2))

def find_gradient(a1,a2,b1,b2):
    n1=b1-a1
    n2=b2-a2
    if n1==0:
        return 0
    else:
        return n2/n1*-1
    
def write_csv(n):
    print(n)
    for i in range(21):
        f.write(str(int(hand_landmarks.landmark[i].x * 255)) + "," + str(int(hand_landmarks.landmark[i].y * 255)) + ",")

    for i in range(1, 4):
        f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_distance(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
                              int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255))) + ",")

    for i in range(5, 8):
        f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_distance(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
                              int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255))) + ",")

    for i in range(9, 12):
        f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_distance(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
                              int(hand_landmarks.landmark[12].x * 255),
                              int(hand_landmarks.landmark[12].y * 255))) + ",")

    for i in range(13, 16):
        f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_distance(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
                              int(hand_landmarks.landmark[16].x * 255),
                              int(hand_landmarks.landmark[16].y * 255))) + ",")

    for i in range(17, 20):
        f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_distance(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
                              int(hand_landmarks.landmark[20].x * 255),
                              int(hand_landmarks.landmark[20].y * 255))) + ",")

    for i in range(1, 4):
        f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_gradient(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
                              int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255))) + ",")

    for i in range(5, 8):
        f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_gradient(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
                              int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255))) + ",")

    for i in range(9, 12):
        f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_gradient(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
                              int(hand_landmarks.landmark[12].x * 255), int(hand_landmarks.landmark[12].y * 255))) + ",")

    for i in range(13, 16):
        f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255),
                                  int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_gradient(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
                              int(hand_landmarks.landmark[16].x * 255), int(hand_landmarks.landmark[16].y * 255))) + ",")

    for i in range(17, 20):
        f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
                                  int(hand_landmarks.landmark[i + 1].x * 255), int(hand_landmarks.landmark[i + 1].y * 255))) + ",")
    f.write(str(find_gradient(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
                              int(hand_landmarks.landmark[20].x * 255), int(hand_landmarks.landmark[20].y * 255))) + "," + str(n) + "\n")


with mp_hands.Hands(max_num_hands=1, min_detection_confidence=0.5, min_tracking_confidence=0.7) as hands:
    while cap.isOpened():
        success, image=cap.read()
        if not success:
            continue

        image=cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image=cv2.flip(image, 1)
        results=hands.process(image)
        image=cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        if results.multi_hand_landmarks:
            for hand_landmarks in results.multi_hand_landmarks:
                finger1=int(hand_landmarks.landmark[4].x * 255)
                finger2=int(hand_landmarks.landmark[8].x * 255)
                dist=abs(finger1-finger2)
                #print(find_distance(int(hand_landmarks.landmark[4].x*255),int(hand_landmarks.landmark[4].y*255),
                #                    int(hand_landmarks.landmark[8].x*255),int(hand_landmarks.landmark[8].y*255)))

                #print(find_gradient(int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255),
                #                    int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255)))



                # cv2.putText(
                #     image, text='f1=%d f2=%d dist=%d ' % (finger1, finger2, dist), org=(10, 30),
                #     fontFace=cv2.FONT_HERSHEY_SIMPLEX, fontScale=1,
                #     color=255, thickness=3)

                mp_drawing.draw_landmarks(image, hand_landmarks, mp_hands.HAND_CONNECTIONS)

        cv2.imshow('image',image)

        # if cv2.waitKey(1)==48:   # 키보드 0 눌렀을때
        #     print("4번 {}   {}".format(int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255)))
        #     print("8번 {}   {}".format(int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255)))
        #     print("거리 : {}".format(find_distance(int(hand_landmarks.landmark[4].x * 255),int(hand_landmarks.landmark[4].y * 255),
        #                                     int(hand_landmarks.landmark[8].x * 255),int(hand_landmarks.landmark[8].y * 255))))
        #     print("기울기 : {}".format(find_gradient(int(hand_landmarks.landmark[4].x * 255),int(hand_landmarks.landmark[4].y * 255),
        #                                     int(hand_landmarks.landmark[8].x * 255),int(hand_landmarks.landmark[8].y * 255))))
        #     print("\n")
        #
        #     for i in range(21):
        #         f.write(str(int(hand_landmarks.landmark[i].x * 255)) + "," + str(int(hand_landmarks.landmark[i].y * 255)) + ",")
        #
        #     for i in range(1,4):
        #         f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_distance(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
        #                               int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255))) + ",")
        #
        #     for i in range(5,8):
        #         f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_distance(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
        #                               int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255))) + ",")
        #
        #     for i in range(9,12):
        #         f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_distance(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
        #                               int(hand_landmarks.landmark[12].x * 255), int(hand_landmarks.landmark[12].y * 255))) + ",")
        #
        #     for i in range(13,16):
        #         f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_distance(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
        #                               int(hand_landmarks.landmark[16].x * 255), int(hand_landmarks.landmark[16].y * 255))) + ",")
        #
        #     for i in range(17,20):
        #         f.write(str(find_distance(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_distance(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
        #                               int(hand_landmarks.landmark[20].x * 255), int(hand_landmarks.landmark[20].y * 255))) + ",")
        #
        #     for i in range(1,4):
        #         f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_gradient(int(hand_landmarks.landmark[1].x * 255), int(hand_landmarks.landmark[1].y * 255),
        #                               int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255))) + ",")
        #
        #     for i in range(5,8):
        #         f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_gradient(int(hand_landmarks.landmark[5].x * 255), int(hand_landmarks.landmark[5].y * 255),
        #                               int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255))) + ",")
        #
        #     for i in range(9,12):
        #         f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_gradient(int(hand_landmarks.landmark[9].x * 255), int(hand_landmarks.landmark[9].y * 255),
        #                               int(hand_landmarks.landmark[12].x * 255), int(hand_landmarks.landmark[12].y * 255))) + ",")
        #
        #     for i in range(13,16):
        #         f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_gradient(int(hand_landmarks.landmark[13].x * 255), int(hand_landmarks.landmark[13].y * 255),
        #                               int(hand_landmarks.landmark[16].x * 255), int(hand_landmarks.landmark[16].y * 255))) + ",")
        #
        #     for i in range(17,20):
        #         f.write(str(find_gradient(int(hand_landmarks.landmark[i].x * 255), int(hand_landmarks.landmark[i].y * 255),
        #                                   int(hand_landmarks.landmark[i+1].x * 255), int(hand_landmarks.landmark[i+1].y * 255))) + ",")
        #     f.write(str(find_gradient(int(hand_landmarks.landmark[17].x * 255), int(hand_landmarks.landmark[17].y * 255),
        #                               int(hand_landmarks.landmark[20].x * 255), int(hand_landmarks.landmark[20].y * 255))) + "," + "0" + "\n")


        # print("4번 {}   {}".format(int(hand_landmarks.landmark[4].x * 255), int(hand_landmarks.landmark[4].y * 255)))
        # print("8번 {}   {}".format(int(hand_landmarks.landmark[8].x * 255), int(hand_landmarks.landmark[8].y * 255)))
        # print("거리 : {}".format(find_distance(int(hand_landmarks.landmark[4].x * 255),int(hand_landmarks.landmark[4].y * 255),
        #                                     int(hand_landmarks.landmark[8].x * 255),int(hand_landmarks.landmark[8].y * 255))))
        # print("기울기 : {}".format(find_gradient(int(hand_landmarks.landmark[4].x * 255),int(hand_landmarks.landmark[4].y * 255),
        #                                     int(hand_landmarks.landmark[8].x * 255),int(hand_landmarks.landmark[8].y * 255))))
        # print("\n")
        
        
        if cv2.waitKey(1)==48:
            write_csv("0")
        elif cv2.waitKey(1)==49:
            write_csv("1")
        elif cv2.waitKey(1)==50:
            write_csv("2")
        elif cv2.waitKey(1)==51:
            write_csv("3")
        elif cv2.waitKey(1)==52:
            write_csv("4")
        elif cv2.waitKey(1)==53:
            write_csv("5")
        elif cv2.waitKey(1)==54:
            write_csv("6")
        elif cv2.waitKey(1)==55:
            write_csv("7")
        elif cv2.waitKey(1)==56:
            write_csv("8")
        elif cv2.waitKey(1)==57:
            write_csv("9")
        elif cv2.waitKey(1)==27:   # 키보드 ESC 입력시 종료
            break



f.close()
cap.release()