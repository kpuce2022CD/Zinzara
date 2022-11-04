import datetime
import time
import os
import pathlib
import pickle
import matplotlib.pyplot as plt
import tensorflow as tf
import numpy as np
from keras.layers import Dense, Flatten, Conv2D, MaxPooling2D, Resizing, BatchNormalization, Activation, Dropout
from keras.models import Sequential
from keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.layers.experimental.preprocessing import Resizing


def preprocess(path, label):
    image = tf.io.read_file(path)
    image = tf.image.decode_jpeg(image, channels=3)
    image = tf.image.resize(image, [image_height, image_width])
    image /= 255.0
    return image, label


# 핸드폰 이미지 크기 width = 1080, height = 1440
# 640, 480 -> 160, 120

BATCH_SIZE = 2
image_width = 120   # 크기도 줄이기
image_height = 160

# train = ImageDataGenerator(rescale=1. / 255, rotation_range=10, width_shift_range=0.1,
#                            height_shift_range=0.1, shear_range=0.1, zoom_range=0.1)

# train = ImageDataGenerator(rescale=1. / 255)
# train_generator = train.flow_from_directory("hand_gesture_data/data3/train/", target_size=(image_width, image_height),
#                                             color_mode="rgb", batch_size=BATCH_SIZE, seed=1, shuffle=True,
#                                             class_mode="sparse")
#
# test = ImageDataGenerator(rescale=1.0 / 255.0)
# test_generator = test.flow_from_directory("hand_gesture_data/data3/test/", target_size=(image_width, image_height),
#                                           color_mode="rgb", batch_size=BATCH_SIZE, seed=2, shuffle=True,
#                                           class_mode="sparse")

train_img_path = pathlib.Path("hand_gesture_data/data4/train")
train_file_list = sorted([str(path) for path in train_img_path.glob("*.jpg")])

test_img_path = pathlib.Path("hand_gesture_data/data4/test")
test_file_list = sorted([str(path) for path in test_img_path.glob("*.jpg")])

train_labels = open("train_label", "rb")
train_y = pickle.load(train_labels)

test_labels = open("test_label", "rb")
test_y = pickle.load(test_labels)

train_data = tf.data.Dataset.from_tensor_slices((train_file_list, train_y))
test_data = tf.data.Dataset.from_tensor_slices((test_file_list, test_y))

train_data = train_data.map(preprocess)
test_data = test_data.map(preprocess)

train_tmp = np.array([])
for i in train_data:
    train_tmp = np.append(train_tmp, i[0].numpy(), axis=0)


model = Sequential()
# model.add(Resizing(image_height, image_width))
model.add(Conv2D(32, (27, 27), padding="same", input_shape=(image_width, image_height, 3)))
model.add(BatchNormalization())
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(MaxPooling2D((2, 2), strides=1))
model.add(Conv2D(128, (27, 27), padding="same"))
model.add(BatchNormalization())
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(MaxPooling2D((2, 2), strides=1))
model.add(Conv2D(64, (27, 27), padding="same"))
model.add(BatchNormalization())
model.add(Activation("relu"))
model.add(Dropout(0.25))
model.add(MaxPooling2D((2, 2), strides=1))
model.add(Flatten())
model.add(Dense(128, activation="relu"))
model.add(Dense(10, activation="softmax"))

model.compile(loss="sparse_categorical_crossentropy", optimizer="adam", metrics=["accuracy"])

start = time.time()

# history = model.fit(train_data, epochs=20, validation_data=test_data, verbose=2)
history = model.fit(train_tmp, train_y, epochs=20, validation_data=test_data, verbose=2)

sec = time.time() - start
time = str(datetime.timedelta(seconds=sec)).split(".")
times = time[0]
print("\ntime =", time)

model.save("qqqqqqqqqqqqqq.h5")

plt.subplot(211)
plt.plot(history.history["accuracy"])
plt.plot(history.history["val_accuracy"])
plt.title("Accuracy")
plt.xlabel("epoch")
plt.ylabel("accuracy")
plt.legend(["train", "validation"], loc="lower right")

plt.subplot(212)
plt.plot(history.history["loss"])
plt.plot(history.history["val_loss"])
plt.title("Loss")
plt.xlabel("epoch")
plt.ylabel("loss")
plt.legend(["train", "validation"], loc="upper right")
plt.tight_layout()
plt.show()





