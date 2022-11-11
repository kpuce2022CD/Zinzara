import tensorflow as tf
tf.config.list_physical_devices("GPU")
from google.colab import drive
drive.mount('/content/gdrive')
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import keras
import random
import copy
from keras.utils import np_utils
from keras.layers import Dense, Flatten, Conv2D, MaxPooling2D, Resizing, BatchNormalization, Activation, Dropout, GlobalAveragePooling2D, concatenate, Rescaling, RandomFlip, RandomRotation, RandomZoom
from keras.models import Sequential
from google.colab import drive
from google.colab import files
from tensorflow.keras.layers.experimental.preprocessing import Resizing, Rescaling
from sklearn.model_selection import train_test_split
from keras.layers import Input
from keras.models import Model


seed = 0
np.random.seed(seed)
tf.random.set_seed(seed)
random.seed(seed)

batch_size = 16
img_height = 240
img_width = 320

Data_set = pd.read_csv("/content/gdrive/MyDrive/Colab Notebooks/qwer.csv")
train_mlp_x_data = Data_set.iloc[:, 20:45].astype(float)
train_mlp_y_data = Data_set.iloc[:, 45]

train_mlp_x_data = train_mlp_x_data.to_numpy()
train_mlp_y_data = np_utils.to_categorical(train_mlp_y_data)

# train_mlp_x_data, test_mlp_x_data, train_mlp_y_data, test_mlp_y_data = train_test_split(train_mlp_x_data, train_mlp_y_data, test_size=0.2, shuffle=False, stratify=train_mlp_y_data, random_state=1)
# train_mlp_x_data, test_mlp_x_data, train_mlp_y_data, test_mlp_y_data = train_test_split(train_mlp_x_data, train_mlp_y_data, test_size=0.2, shuffle=False, random_state=1)


train_cnn_x_data = np.load("/content/gdrive/MyDrive/Colab Notebooks/train_cnn_x_data_No_val.npy")
train_cnn_y_data = np.load("/content/gdrive/MyDrive/Colab Notebooks/train_cnn_y_data_No_val.npy")


# Shuffle
tmp1 = []
tmp2 = []
tmp3 = []
i = 0
for _ in range(875):
    tmp1.append(train_mlp_x_data[i:i + 2].copy())
    tmp2.append(train_mlp_y_data[i:i + 2].copy())
    tmp3.append(train_cnn_x_data[i:i + 2].copy())
    i += 2

i = 0
cnt = 0
while cnt < 875:
    n = random.randint(0, 874 - cnt)
    train_mlp_x_data[i:i + 2] = copy.deepcopy(tmp1[n])
    train_mlp_y_data[i:i + 2] = copy.deepcopy(tmp2[n])
    train_cnn_x_data[i:i + 2] = copy.deepcopy(tmp3[n])
    tmp1.pop(n)
    tmp2.pop(n)
    tmp3.pop(n)
    cnt += 1
    i += 2


print("train_cnn_x_data shape :", train_cnn_x_data.shape)
print("train_cnn_y_data shape :", train_cnn_y_data.shape)
print("train_mlp_x_data shape :", train_mlp_x_data.shape)
print("train_mlp_y_data shape :", train_mlp_y_data.shape)

print(type(train_cnn_x_data))
print(type(train_mlp_x_data))
print(type(train_mlp_y_data))

test_mlp_x_data = train_mlp_x_data[:500:-1].copy()
test_mlp_y_data = train_mlp_y_data[:500:-1].copy()
test_cnn_x_data = train_cnn_x_data[:500:-1].copy()


# MLP~~~~~~~~~~~~~~~~~~~~
mlp_input = Input(shape=(25, ), name="mlp_input")
mlp_hidden1 = Dense(64)(mlp_input)
mlp_hidden1 = BatchNormalization()(mlp_hidden1)
mlp_hidden1 = Activation("relu")(mlp_hidden1)
mlp_hidden1 = Dropout(0.5)(mlp_hidden1)

mlp_hidden2 = Dense(128)(mlp_hidden1)
mlp_hidden2 = BatchNormalization()(mlp_hidden2)
mlp_hidden2 = Activation("relu")(mlp_hidden2)
mlp_hidden2 = Dropout(0.5)(mlp_hidden2)

mlp_hidden3 = Dense(256)(mlp_hidden2)
mlp_hidden3 = BatchNormalization()(mlp_hidden3)
mlp_hidden3 = Activation("relu")(mlp_hidden3)
mlp_hidden3 = Dropout(0.5)(mlp_hidden3)

mlp_hidden4 = Dense(512)(mlp_hidden3)
mlp_hidden4 = BatchNormalization()(mlp_hidden4)
mlp_hidden4 = Activation("relu")(mlp_hidden4)
mlp_hidden4 = Dropout(0.5)(mlp_hidden4)

mlp_hidden5 = Dense(256)(mlp_hidden4)
mlp_hidden5 = BatchNormalization()(mlp_hidden5)
mlp_hidden5 = Activation("relu")(mlp_hidden5)
mlp_hidden5 = Dropout(0.5)(mlp_hidden5)

mlp_hidden6 = Dense(128)(mlp_hidden5)
mlp_hidden6 = BatchNormalization()(mlp_hidden6)
mlp_hidden6 = Activation("relu")(mlp_hidden6)
mlp_hidden6 = Dropout(0.5)(mlp_hidden6)

mlp_hidden7 = Dense(64)(mlp_hidden6)
mlp_hidden7 = BatchNormalization()(mlp_hidden7)
mlp_hidden7 = Activation("relu")(mlp_hidden7)
mlp_hidden7 = Dropout(0.5)(mlp_hidden7)

# mlp_output = Dense(10, activation="softmax")(mlp_hidden6)
mlp_output = Dense(40, name="mlp_output")(mlp_hidden7)
mlp_model = Model(inputs=mlp_input, outputs=mlp_output)


data_augmentation = keras.Sequential(
    [
        Rescaling(1. / 255),
        RandomFlip("horizontal"),
        RandomRotation(0.2),
        RandomZoom(0.2),
    ]
)


# CNN~~~~~~~~~~~~~~~~~~~~
cnn_input = tf.keras.Input((img_height, img_width, 3), name="cnn_input")
# cnn_input = data_augmentation(cnn_input)

# conv_1
conv1 = Conv2D(64, (7, 7), padding="same", strides=2)(cnn_input)
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
conv2_1 = Activation("relu")(conv2_1)
conv2_1 = Dropout(0.5)(conv2_1)
short_cut = Conv2D(64, (1, 1), padding="same", strides=1)(conv1)
conv2_1 = tf.keras.layers.Add()([conv2_1, short_cut])
conv2_1 = BatchNormalization()(conv2_1)
conv2_1 = Activation("relu")(conv2_1)

conv2_2 = Conv2D(64, (3, 3), padding="same", strides=1)(conv2_1)
conv2_2 = BatchNormalization()(conv2_2)
conv2_2 = Activation("relu")(conv2_2)
conv2_2 = Dropout(0.5)(conv2_2)
conv2_2 = Conv2D(64, (3, 3), padding="same", strides=1)(conv2_2)
conv2_2 = BatchNormalization()(conv2_2)
conv2_2 = Activation("relu")(conv2_2)
conv2_2 = Dropout(0.5)(conv2_2)
conv2_2 = tf.keras.layers.Add()([conv2_2, conv2_1])
conv2_2 = BatchNormalization()(conv2_2)
conv2_2 = Activation("relu")(conv2_2)

conv2_3 = Conv2D(64, (3, 3), padding="same", strides=1)(conv2_2)
conv2_3 = BatchNormalization()(conv2_3)
conv2_3 = Activation("relu")(conv2_3)
conv2_3 = Dropout(0.5)(conv2_3)
conv2_3 = Conv2D(64, (3, 3), padding="same", strides=1)(conv2_3)
conv2_3 = BatchNormalization()(conv2_3)
conv2_3 = Activation("relu")(conv2_3)
conv2_3 = Dropout(0.5)(conv2_3)
conv2_3 = tf.keras.layers.Add()([conv2_3, conv2_2])
conv2_3 = BatchNormalization()(conv2_3)
conv2_3 = Activation("relu")(conv2_3)

# conv_3_x
conv3_1 = Conv2D(128, (3, 3), padding="same", strides=2)(conv2_3)
conv3_1 = BatchNormalization()(conv3_1)
conv3_1 = Activation("relu")(conv3_1)
conv3_1 = Dropout(0.5)(conv3_1)
conv3_1 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_1)
conv3_1 = BatchNormalization()(conv3_1)
conv3_1 = Activation("relu")(conv3_1)
conv3_1 = Dropout(0.5)(conv3_1)
short_cut = Conv2D(128, (1, 1), padding="same", strides=2)(conv2_3)
conv3_1 = tf.keras.layers.Add()([conv3_1, short_cut])
conv3_1 = BatchNormalization()(conv3_1)
conv3_1 = Activation("relu")(conv3_1)

conv3_2 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_1)
conv3_2 = BatchNormalization()(conv3_2)
conv3_2 = Activation("relu")(conv3_2)
conv3_2 = Dropout(0.5)(conv3_2)
conv3_2 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_2)
conv3_2 = BatchNormalization()(conv3_2)
conv3_2 = Activation("relu")(conv3_2)
conv3_2 = Dropout(0.5)(conv3_2)
conv3_2 = tf.keras.layers.Add()([conv3_2, conv3_1])
conv3_2 = BatchNormalization()(conv3_2)
conv3_2 = Activation("relu")(conv3_2)

conv3_3 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_2)
conv3_3 = BatchNormalization()(conv3_3)
conv3_3 = Activation("relu")(conv3_3)
conv3_3 = Dropout(0.5)(conv3_3)
conv3_3 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_3)
conv3_3 = BatchNormalization()(conv3_3)
conv3_3 = Activation("relu")(conv3_3)
conv3_3 = Dropout(0.5)(conv3_3)
conv3_3 = tf.keras.layers.Add()([conv3_3, conv3_2])
conv3_3 = BatchNormalization()(conv3_3)
conv3_3 = Activation("relu")(conv3_3)

conv3_4 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_3)
conv3_4 = BatchNormalization()(conv3_4)
conv3_4 = Activation("relu")(conv3_4)
conv3_4 = Dropout(0.5)(conv3_4)
conv3_4 = Conv2D(128, (3, 3), padding="same", strides=1)(conv3_4)
conv3_4 = BatchNormalization()(conv3_4)
conv3_4 = Activation("relu")(conv3_4)
conv3_4 = Dropout(0.5)(conv3_4)
conv3_4 = tf.keras.layers.Add()([conv3_4, conv3_3])
conv3_4 = BatchNormalization()(conv3_4)
conv3_4 = Activation("relu")(conv3_4)

# conv_4_x
conv4_1 = Conv2D(256, (3, 3), padding="same", strides=2)(conv3_4)
conv4_1 = BatchNormalization()(conv4_1)
conv4_1 = Activation("relu")(conv4_1)
conv4_1 = Dropout(0.5)(conv4_1)
conv4_1 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_1)
conv4_1 = BatchNormalization()(conv4_1)
conv4_1 = Activation("relu")(conv4_1)
conv4_1 = Dropout(0.5)(conv4_1)
short_cut = Conv2D(256, (1, 1), padding="same", strides=2)(conv3_4)
conv4_1 = tf.keras.layers.Add()([conv4_1, short_cut])
conv4_1 = BatchNormalization()(conv4_1)
conv4_1 = Activation("relu")(conv4_1)

conv4_2 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_1)
conv4_2 = BatchNormalization()(conv4_2)
conv4_2 = Activation("relu")(conv4_2)
conv4_2 = Dropout(0.5)(conv4_2)
conv4_2 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_2)
conv4_2 = BatchNormalization()(conv4_2)
conv4_2 = Activation("relu")(conv4_2)
conv4_2 = Dropout(0.5)(conv4_2)
conv4_2 = tf.keras.layers.Add()([conv4_2, conv4_1])
conv4_2 = BatchNormalization()(conv4_2)
conv4_2 = Activation("relu")(conv4_2)

conv4_3 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_2)
conv4_3 = BatchNormalization()(conv4_3)
conv4_3 = Activation("relu")(conv4_3)
conv4_3 = Dropout(0.5)(conv4_3)
conv4_3 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_3)
conv4_3 = BatchNormalization()(conv4_3)
conv4_3 = Activation("relu")(conv4_3)
conv4_3 = Dropout(0.5)(conv4_3)
conv4_3 = tf.keras.layers.Add()([conv4_3, conv4_2])
conv4_3 = BatchNormalization()(conv4_3)
conv4_3 = Activation("relu")(conv4_3)

conv4_4 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_3)
conv4_4 = BatchNormalization()(conv4_4)
conv4_4 = Activation("relu")(conv4_4)
conv4_4 = Dropout(0.5)(conv4_4)
conv4_4 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_4)
conv4_4 = BatchNormalization()(conv4_4)
conv4_4 = Activation("relu")(conv4_4)
conv4_4 = Dropout(0.5)(conv4_4)
conv4_4 = tf.keras.layers.Add()([conv4_4, conv4_3])
conv4_4 = BatchNormalization()(conv4_4)
conv4_4 = Activation("relu")(conv4_4)

conv4_5 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_4)
conv4_5 = BatchNormalization()(conv4_5)
conv4_5 = Activation("relu")(conv4_5)
conv4_5 = Dropout(0.5)(conv4_5)
conv4_5 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_5)
conv4_5 = BatchNormalization()(conv4_5)
conv4_5 = Activation("relu")(conv4_5)
conv4_5 = Dropout(0.5)(conv4_5)
conv4_5 = tf.keras.layers.Add()([conv4_5, conv4_4])
conv4_5 = BatchNormalization()(conv4_5)
conv4_5 = Activation("relu")(conv4_5)

conv4_6 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_5)
conv4_6 = BatchNormalization()(conv4_6)
conv4_6 = Activation("relu")(conv4_6)
conv4_6 = Dropout(0.5)(conv4_6)
conv4_6 = Conv2D(256, (3, 3), padding="same", strides=1)(conv4_6)
conv4_6 = BatchNormalization()(conv4_6)
conv4_6 = Activation("relu")(conv4_6)
conv4_6 = Dropout(0.5)(conv4_6)
conv4_6 = tf.keras.layers.Add()([conv4_6, conv4_5])
conv4_6 = BatchNormalization()(conv4_6)
conv4_6 = Activation("relu")(conv4_6)

# conv_5_x
conv5_1 = Conv2D(512, (3, 3), padding="same", strides=2)(conv4_6)
conv5_1 = BatchNormalization()(conv5_1)
conv5_1 = Activation("relu")(conv5_1)
conv5_1 = Dropout(0.5)(conv5_1)
conv5_1 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_1)
conv5_1 = BatchNormalization()(conv5_1)
conv5_1 = Activation("relu")(conv5_1)
conv5_1 = Dropout(0.5)(conv5_1)
short_cut = Conv2D(512, (1, 1), padding="same", strides=2)(conv4_6)
conv5_1 = tf.keras.layers.Add()([conv5_1, short_cut])
conv5_1 = BatchNormalization()(conv5_1)
conv5_1 = Activation("relu")(conv5_1)

conv5_2 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_1)
conv5_2 = BatchNormalization()(conv5_2)
conv5_2 = Activation("relu")(conv5_2)
conv5_2 = Dropout(0.5)(conv5_2)
conv5_2 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_2)
conv5_2 = BatchNormalization()(conv5_2)
conv5_2 = Activation("relu")(conv5_2)
conv5_2 = Dropout(0.5)(conv5_2)
conv5_2 = tf.keras.layers.Add()([conv5_2, conv5_1])
conv5_2 = BatchNormalization()(conv5_2)
conv5_2 = Activation("relu")(conv5_2)

conv5_3 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_2)
conv5_3 = BatchNormalization()(conv5_3)
conv5_3 = Activation("relu")(conv5_3)
conv5_3 = Dropout(0.5)(conv5_3)
conv5_3 = Conv2D(512, (3, 3), padding="same", strides=1)(conv5_3)
conv5_3 = BatchNormalization()(conv5_3)
conv5_3 = Activation("relu")(conv5_3)
conv5_3 = Dropout(0.5)(conv5_3)
conv5_3 = tf.keras.layers.Add()([conv5_3, conv5_2])
conv5_3 = BatchNormalization()(conv5_3)
conv5_3 = Activation("relu")(conv5_3)

avg_pool = GlobalAveragePooling2D()(conv5_3)
flat = Flatten()(avg_pool)
# cnn_output = Dense(10, activsation="softmax")(flat)
cnn_output = Dense(40, name="cnn_output")(flat)
cnn_model = Model(inputs=cnn_input, outputs=cnn_output)


concatenated = concatenate([mlp_output, cnn_output])
concatenated = Dense(40, activation="relu")(concatenated)
concat_output = Dense(10, activation="softmax")(concatenated)

model = Model(inputs=[mlp_input, cnn_input], outputs=concat_output)

model.compile(optimizer="adam", loss="categorical_crossentropy", metrics=["accuracy"])

# print("train_mlp_x_data :", len(train_mlp_x_data))
# print("train_mlp_x_data.shape :", train_mlp_x_data.shape)
# print("train_mlp_y_data :", len(train_mlp_y_data))
# print("train_mlp_y_data.shape :", train_mlp_y_data.shape)
print()

# model.fit({"mlp_input": train_mlp_x_data, "cnn_input": train_cnn_x_data}, {"mlp_output": train_mlp_y_data, "cnn_output": train_cnn_y_data}, epochs=20, verbose=2)
model.fit({"mlp_input": train_mlp_x_data, "cnn_input": train_cnn_x_data}, train_mlp_y_data, validation_data=([test_mlp_x_data, test_cnn_x_data], test_mlp_y_data), batch_size=batch_size, epochs=30, verbose=2)

model.save("/content/gdrive/MyDrive/Colab Notebooks/multi_input.h5")