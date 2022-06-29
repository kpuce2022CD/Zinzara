import tensorflow as tf
import matplotlib.pyplot as plt
import tensorflow_hub as hub
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten, Conv2D, MaxPooling2D, GlobalMaxPool2D, GlobalAveragePooling2D
from keras.preprocessing.image import ImageDataGenerator
import time, datetime
from tensorflow.keras.applications import ResNet50


# model = Sequential([hub.KerasLayer("https://tfhub.dev/google/imagenet/resnet_v2_152/feature_vector/4",
#                                    input_shape=(image_width, image_height, 3), trainable=False),
#                     Dense(10, activation="softmax")])
#
# train = ImageDataGenerator(rescale=1. / 255, rotation_range=20, width_shift_range=0.1,
#                            height_shift_range=0.1, shear_range=0.2, zoom_range=0.3)
# train_generator = train.flow_from_directory("hand_gesture_data/data3/train", target_size=(image_height, image_width),
#                                             color_mode="rgb", batch_size=BATCH_SIZE, seed=1, shuffle=True,
#                                             class_mode="categorical")
#
# test = ImageDataGenerator(rescale=1.0 / 255.0)
# test_generator = test.flow_from_directory("hand_gesture_data/data3/test", target_size=(image_height, image_width),
#                                           color_mode="rgb", batch_size=BATCH_SIZE, seed=2, shuffle=True,
#                                           class_mode="categorical")
#
# model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])
#
# history = model.fit(train_generator, validation_data=test_generator, epochs=10, verbose=2)
#
# accuracy = history.history["accuracy"]
# loss = history.history["loss"]


BATCH_SIZE = 2
image_width = 640
image_height = 480

train = ImageDataGenerator(rescale=1. / 255, rotation_range=10, width_shift_range=0.1,
                           height_shift_range=0.1, shear_range=0.1, zoom_range=0.1)
train_generator = train.flow_from_directory("hand_gesture_data/data3/train/", target_size=(image_width, image_height),
                                            color_mode="rgb", batch_size=BATCH_SIZE, seed=1, shuffle=True,
                                            class_mode="sparse")

test = ImageDataGenerator(rescale=1.0 / 255.0)
test_generator = test.flow_from_directory("hand_gesture_data/data3/test/", target_size=(image_width, image_height),
                                          color_mode="rgb", batch_size=BATCH_SIZE, seed=2, shuffle=True,
                                          class_mode="sparse")

model = Sequential()
model.add(Conv2D(32, (3, 3), padding="same", activation="relu", input_shape=(image_width, image_height, 3)))
model.add(MaxPooling2D((2, 2), strides=2))
model.add(Conv2D(64, (3, 3), padding="same", activation="relu"))
model.add(MaxPooling2D((2, 2), strides=2))
model.add(Flatten())
model.add(Dense(128, activation="relu"))
model.add(Dense(10, activation="softmax"))

model.compile(loss="sparse_categorical_crossentropy", optimizer="adam", metrics=["accuracy"])

start = time.time()

history = model.fit(train_generator, epochs=20, validation_data=test_generator, verbose=2)

sec = time.time() - start
time = str(datetime.timedelta(seconds=sec)).split(".")
times = time[0]
print("\ntime =", time)

model.save("model_cnn_batch2_epoch20.h5")

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





