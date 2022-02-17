from rest_framework import serializers
from .models import Gestures
from django.core.files.base import ContentFile
import uuid
import base64

class GesturesSerializer(serializers.ModelSerializer):
    # class Meta:
    #     model = Gestures
    #     fields = ["img", "base64"]
    #
    # def create(self, validated_data):
    #     img_name = str(uuid.uuid4())
    #     base64 = validated_data.get("base64", None)
    #     img = ContentFile(base64.b64decode(validated_data.get("base64", None)), name=img_name+".png")
    #     return
    class Meta:
        model = Gestures
        fields = ["img"]