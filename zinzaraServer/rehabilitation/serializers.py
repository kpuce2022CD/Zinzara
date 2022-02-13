from rest_framework import serializers
from .models import Physical_rehabilitation
from .models import Language_rehabilitation


class PhysicalSerializer(serializers.ModelSerializer):
    class Meta:
        model = Physical_rehabilitation
        #fields = ["user_id", "physical_score", "rehabilitation_time", "physical_idx"]
        fields = ["user_id", "physical_score", "rehabilitation_time"]

class LanguageSerializer(serializers.ModelSerializer):
    class Meta:
        model = Language_rehabilitation
        #fields = ["user_id", "language_score", "rehabilitation_time", "language_idx"]
        fields = ["user_id", "language_score", "rehabilitation_time"]
