from django.db import models

# Create your models here.


class Gestures(models.Model):
    #img = models.ImageField(upload_to="images/")
    #base64 = models.TextField(blank=True)
    img = models.TextField(default='')

