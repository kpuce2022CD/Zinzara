import tensorflow as tf
import datetime
import time
import matplotlib.pyplot as plt
import tensorflow as tf
from keras.layers import Dense, Flatten, Conv2D, MaxPooling2D, Resizing, BatchNormalization, Activation, Dropout, GlobalAveragePooling2D
from keras.models import Sequential
from tensorflow.keras.layers.experimental.preprocessing import Resizing, Rescaling
from keras.preprocessing.image import ImageDataGenerator

batch_size = 32
img_height = 320
img_width = 240

# drive.mount('/content/gdrive')

# file_path1 = "/content/gdrive/MyDrive/Colab Notebooks/data2/"
# file_path2 = "/content/gdrive/MyDrive/Colab Notebooks/data3/"


# train_data = tf.keras.utils.image_dataset_from_directory(file_path1, labels="inferred", label_mode="int",
#                                                          batch_size=batch_size, image_size=(img_height, img_width),
#                                                          shuffle=True, seed=1, validation_split=0.2, subset="training")
# test_data = tf.keras.utils.image_dataset_from_directory(file_path1, labels="inferred", label_mode="int",
#                                                         batch_size=batch_size, image_size=(img_height, img_width),
#                                                         shuffle=True, seed=1, validation_split=0.2, subset="validation")


train_data = ImageDataGenerator(rescale=1. / 255, rotation_range=30, width_shift_range=0.5,
                           height_shift_range=0.5, shear_range=0.5, zoom_range=0.5, horizontal_flip=True, vertical_flip=True)

train_data = train_data.flow_from_directory("/content/gdrive/MyDrive/Colab Notebooks/data2/", target_size=(img_height, img_width),
                                            color_mode="rgb", batch_size=batch_size, seed=1, shuffle=True,
                                            class_mode="sparse")

test_data = ImageDataGenerator(rescale=1. / 255)
test_data = test_data.flow_from_directory("/content/gdrive/MyDrive/Colab Notebooks/data2/", target_size=(img_height, img_width),
                                          color_mode="rgb", batch_size=batch_size, seed=1, shuffle=True,
                                          class_mode="sparse")



# Input tensor
x = tf.keras.Input((img_height, img_width, 3))
# conv_1
conv1 = Conv2D(64, (7, 7), padding="same", strides=2)(x)
conv1 = BatchNormalization()(conv1)
conv1 = Activation("relu")(conv1)
conv1 = MaxPooling2D((3, 3), padding="SAME", strides=2)(conv1)

# conv_2_x
conv2_1 = Conv2D(64, (3, 3), padding="same", strides=1)(conv1)
conv2_1 = BatchNormalization()(conv2_1)
conv2_1 = Activation("relu")(conv2_1)
conv2_1 = Dropout(0.5)(conv2_1)
conv2_1 = Conv2D(64, (3, 3), padding="same", strides=1)(conv2_1)
conv2_1 = BatchNormalization()(conv2_1)
short_cut = Conv2D(64, (1, 1), padding="same", strides=1)(conv1)
conv2_1 = tf.keras.layers.Add()([conv2_1, short_cut])
conv2_1 = Activation("relu")(conv2_1)

conv2_2 = Conv2D(64, (3, 3), padding="same", strides=1)(conv2_1)
conv2_2 = BatchNormalization()(conv2_2)
conv2_2 = Activation("relu")(conv2_2)
conv2_2 = Dropout(0.5)(conv2_2)
conv2_2 = Conv2D(64, (3, 3), padding="same", strides=1)(conv2_2)
conv2_2 = BatchNormalization()(conv2_2)
conv2_2 = tf.keras.layers.Add()([conv2_2, conv2_1])
conv2_2 = Activation("relu")(conv2_2)

# conv_3_x
conv3_1 = Conv2D(128, (3, 3), padding="same", strides=2)(conv2_2)
conv3_1 = BatchNormalization()(conv3_1)
conv3_1 = Activation("relu")(conv3_1)
conv3_1 = Dropout(0.5)(conv3_1)
conv3_1 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_1)
conv3_1 = BatchNormalization()(conv3_1)
short_cut = Conv2D(128, (1, 1), padding="same", strides=2)(conv2_2)
conv3_1 = tf.keras.layers.Add()([conv3_1, short_cut])
conv3_1 = Activation("relu")(conv3_1)

conv3_2 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_1)
conv3_2 = BatchNormalization()(conv3_2)
conv3_2 = Activation("relu")(conv3_2)
conv3_2 = Dropout(0.5)(conv3_2)
conv3_2 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_2)
conv3_2 = BatchNormalization()(conv3_2)
conv3_2 = tf.keras.layers.Add()([conv3_2, conv3_1])
conv3_2 = Activation("relu")(conv3_2)

# conv_4_x
conv4_1 = Conv2D(256, (3, 3), padding="same", strides=2)(conv3_2)
conv4_1 = BatchNormalization()(conv4_1)
conv4_1 = Activation("relu")(conv4_1)
conv4_1 = Dropout(0.5)(conv4_1)
conv4_1 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_1)
conv4_1 = BatchNormalization()(conv4_1)
short_cut = Conv2D(256, (1, 1), padding="same", strides=2)(conv3_2)
conv4_1 = tf.keras.layers.Add()([conv4_1, short_cut])
conv4_1 = Activation("relu")(conv4_1)

conv4_2 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_1)
conv4_2 = BatchNormalization()(conv4_2)
conv4_2 = Activation("relu")(conv4_2)
conv4_2 = Dropout(0.5)(conv4_2)
conv4_2 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_2)
conv4_2 = BatchNormalization()(conv4_2)
conv4_2 = tf.keras.layers.Add()([conv4_2, conv4_1])
conv4_2 = Activation("relu")(conv4_2)

# conv_5_x
conv5_1 = Conv2D(512, (3, 3), padding="same", strides=2)(conv4_2)
conv5_1 = BatchNormalization()(conv5_1)
conv5_1 = Activation("relu")(conv5_1)
conv5_1 = Dropout(0.5)(conv5_1)
conv5_1 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_1)
conv5_1 = BatchNormalization()(conv5_1)
short_cut = Conv2D(512, (1, 1), padding="same", strides=2)(conv4_2)
conv5_1 = tf.keras.layers.Add()([conv5_1, short_cut])
conv5_1 = Activation("relu")(conv5_1)

conv5_2 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_1)
conv5_2 = BatchNormalization()(conv5_2)
conv5_2 = Activation("relu")(conv5_2)
conv5_2 = Dropout(0.5)(conv5_2)
conv5_2 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_2)
conv5_2 = BatchNormalization()(conv5_2)
conv5_2 = tf.keras.layers.Add()([conv5_2, conv5_1])
conv5_2 = Activation("relu")(conv5_2)

avg_pool = GlobalAveragePooling2D()(conv5_2)
flat = Flatten()(avg_pool)
dense10 = Dense(10, activation="softmax")(flat)

model = tf.keras.Model(inputs=x, outputs=dense10)


model.compile(loss="sparse_categorical_crossentropy", optimizer="adam", metrics=["accuracy"])

start = time.time()

history = model.fit(train_data, epochs=20, validation_data=test_data, verbose=2)
model.evaluate(test_data)

sec = time.time() - start
time = str(datetime.timedelta(seconds=sec)).split(".")
times = time[0]
print("\ntime =", time)

# model.save("qwe.h5")
# files.download("qwe.h5")
# model.save("./qwe.h5")
# model.save("/content/gdrive/MyDrive/Colab Notebooks/data2/qwe.h5")

# plt.subplot(211)
# plt.plot(history.history["accuracy"])
# plt.plot(history.history["val_accuracy"])
# plt.title("Accuracy")
# plt.xlabel("epoch")
# plt.ylabel("accuracy")
# plt.legend(["train", "validation"], loc="lower right")

# plt.subplot(212)
# plt.plot(history.history["loss"])
# plt.plot(history.history["val_loss"])
# plt.title("Loss")
# plt.xlabel("epoch")
# plt.ylabel("loss")
# plt.legend(["train", "validation"], loc="upper right")
# plt.tight_layout()
# plt.show()