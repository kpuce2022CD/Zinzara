import tensorflow as tf
import numpy as np
from keras.preprocessing.image import ImageDataGenerator

seed = 0
np.random.seed(seed)
tf.random.set_seed(seed)

batch_size = 2
img_height = 240
img_width = 320

train_cnn_data = ImageDataGenerator(rescale=1. / 255, rotation_range=50, width_shift_range=0.6, height_shift_range=0.6,
                                    shear_range=0.6, zoom_range=0.6, horizontal_flip=True, vertical_flip=True, validation_split=0.2)


train_cnn_data = train_cnn_data.flow_from_directory("/content/gdrive/MyDrive/Colab Notebooks/data2/", target_size=(img_height, img_width),
                                            color_mode="rgb", batch_size=batch_size, seed=1, shuffle=True, class_mode="categorical", subset="training")


test_cnn_data = ImageDataGenerator(rescale=1. / 255, validation_split=0.2)
test_cnn_data = test_cnn_data.flow_from_directory("/content/gdrive/MyDrive/Colab Notebooks/data2/", target_size=(img_height, img_width),
                                         color_mode="rgb", batch_size=batch_size, seed=2, shuffle=True, class_mode="categorical", subset="validation")


train_cnn_data = tf.keras.utils.image_dataset_from_directory("/content/gdrive/MyDrive/Colab Notebooks/data2/", labels="inferred",
                                                             label_mode="categorical", batch_size=batch_size, image_size=(img_height, img_width),
                                                             shuffle=False, seed=1)

test_cnn_data = tf.keras.utils.image_dataset_from_directory("/content/gdrive/MyDrive/Colab Notebooks/data2/", labels="inferred", label_mode="categorical",
                                                             batch_size=batch_size, image_size=(img_height, img_width), shuffle=False, seed=1, validation_split=0.2, subset="validation")


a = []
b = []

print(len(train_cnn_data))
print(len(test_cnn_data))


for items in train_cnn_data:
    a.append(items[0])
    b.append(items[1])

train_cnn_x_data = np.concatenate(a, axis=0)
train_cnn_y_data = np.concatenate(b, axis=0)

print(train_cnn_x_data.shape)
print(train_cnn_y_data.shape)

np.save("/content/gdrive/MyDrive/Colab Notebooks/train_cnn_x_data_No_val.npy", train_cnn_x_data)
np.save("/content/gdrive/MyDrive/Colab Notebooks/train_cnn_y_data_No_val.npy", train_cnn_y_data)
