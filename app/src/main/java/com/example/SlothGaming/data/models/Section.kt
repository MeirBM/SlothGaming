package com.example.SlothGaming.data.models

import androidx.annotation.StringRes

data class Section(@StringRes val titleRes: Int, val items: List<GameItem>)