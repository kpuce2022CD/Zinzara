import tensorflow as tf
import datetime
import time
import matplotlib.pyplot as plt
from keras.layers import Dense, Flatten, Conv2D, MaxPooling2D, Resizing, BatchNormalization, Activation, Dropout, GlobalAveragePooling2D
from keras.models import Sequential
from tensorflow.keras.layers.experimental.preprocessing import Resizing, Rescaling
from keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.applications import ResNet50
from tensorflow import keras
from tensorflow.keras import layers


batch_size = 32
img_height = 240
img_width = 320

# train_data = tf.keras.utils.image_dataset_from_directory(file_path1, labels="inferred", label_mode="int",
#                                                          batch_size=batch_size, image_size=(img_height, img_width),
#                                                          shuffle=True, seed=1, validation_split=0.2, subset="training")
# test_data = tf.keras.utils.image_dataset_from_directory(file_path1, labels="inferred", label_mode="int",
#                                                         batch_size=batch_size, image_size=(img_height, img_width),
#                                                         shuffle=True, seed=1, validation_split=0.2, subset="validation")



train_data = ImageDataGenerator(rescale=1. / 255, rotation_range=50, width_shift_range=0.2,
                           height_shift_range=0.2, shear_range=0.2, zoom_range=0.2, horizontal_flip=True, vertical_flip=True, validation_split=0.)

train_data = train_data.flow_from_directory("/content/gdrive/MyDrive/Colab Notebooks/train/", target_size=(img_height, img_width),
                                            color_mode="rgb", batch_size=batch_size, seed=1, shuffle=True,
                                            class_mode="categorical")

test_data = ImageDataGenerator(rescale=1. / 255, validation_split=0.)
test_data = test_data.flow_from_directory("/content/gdrive/MyDrive/Colab Notebooks/test/", target_size=(img_height, img_width),
                                          color_mode="rgb", batch_size=batch_size, seed=1, shuffle=True,
                                          class_mode="categorical")

base = ResNet50(weights="imagenet", input_shape=(img_height, img_width, 3), include_top=False)
base.trainable = False

model = keras.Sequential()
model.add(base)
model.add(layers.Flatten())
model.add(layers.Dense(64, activation="relu"))
model.add(layers.Dense(32, activation="softmax"))
model.add(layers.Dense(10, activation="softmax"))

model.compile(optimizer="adam", loss="categorical_crossentropy", metrics="accuracy")

history = model.fit(train_data, batch_size=4, epochs=10, validation_data=test_data)

model.evaluate(test_data)

