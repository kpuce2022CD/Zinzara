import matplotlib.pyplot as plt
import numpy as np
import os
import PIL
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.models import Sequential

img_height = 320
img_width = 240

test_data = tf.keras.utils.image_dataset_from_directory("hand_gesture_data/data1/", labels="inferred", label_mode="int",
                                                        image_size=(img_height, img_width), shuffle=True, seed=1)


model = keras.models.load_model("qwer.h5")
model.evaluate(test_data)


for i in range(10):
    img = tf.keras.preprocessing.image.load_img("./" + str(i) + ".jpg", target_size=(img_height, img_width))
    img = tf.keras.preprocessing.image.img_to_array(img)
    img = np.array([img])

    predictions = model.predict(img)
    predictions = np.argmax(predictions, axis=1)
    print(predictions)



