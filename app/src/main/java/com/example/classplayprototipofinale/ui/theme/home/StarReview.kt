package com.example.classplayprototipofinale.ui.theme.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.classplayprototipofinale.ClassPlayViewModel
import com.example.classplayprototipofinale.R
import com.example.classplayprototipofinale.models.Cosplay
import com.example.classplayprototipofinale.models.Review
import com.example.classplayprototipofinale.ui.theme.StarCol
import com.google.firebase.database.DatabaseReference
import kotlin.math.floor

class StarReview {

    /** Stelle della recensione dell'utente **/

    @Composable
    fun UpdateReview(i: Int, size: Float, cosReview: Long) {

        Row {
            Image(painter = painterResource(id =
            if (cosReview.toInt() >= i)
                R.drawable.star_1
            else
                R.drawable.star_0),
                colorFilter = ColorFilter.tint(
                    if (cosReview.toInt() >= i)
                        StarCol
                    else
                        Color.White
                ) , contentDescription = "Star number $i", modifier = Modifier
                .size(size.dp)
                .padding(start = 3.dp)
            )
        }
    }

    /** Stelle delle recensioni globali cliccabili per aggiungere una recensione **/

    @Composable
    fun NewReview(cpvm: ClassPlayViewModel, i: Int, cpDB: DatabaseReference, cosplay: Cosplay, avgReview: String?, size: Float) {

        Image(painter = painterResource(id = R.drawable.star_0), contentDescription = "Star number $i", modifier = Modifier
            .size(size.dp)
            .padding(start = 3.dp)
            .clickable {
                saveAvarageAdd(cpvm, cosplay, i, cpDB)
            })

    }

    /** Stelle delle recensioni globali non cliccabili **/

    @Composable
    fun AvgReviews(avg: String, i: Int, size: Float) {

        val avgReview = avg.replace(',', '.').toFloat() ?: 0.0f

        Image(painter = painterResource(id =
        if (avgReview / i >= 1)
            R.drawable.star_1

        else if ((i - floor(avgReview)).toInt() == 1 && (avgReview - floor(avgReview)).toFloat() == 0.0f)
            R.drawable.star_0

        else if ((i - floor(avgReview)).toInt() == 1 && avgReview - floor(avgReview) <= 0.3)
            R.drawable.star_13

        else if ((i - floor(avgReview)).toInt() == 1 && avgReview - floor(avgReview) >= 0.7)
            R.drawable.star_23

        else if ((i - floor(avgReview)).toInt() != 1)
            R.drawable.star_0

        else
            R.drawable.star_12), contentDescription = "Star number $i", modifier = Modifier
            .size(size.dp)
            .padding(start = 3.dp))

    }

    fun saveAvarageDelete(cpDB: DatabaseReference, cpvm: ClassPlayViewModel, cosplay: Cosplay) {
        cpDB
            .child(cosplay.cosplayName!!)
            .child("reviews")
            .child(cpvm.username.value!!)
            .removeValue()
            .addOnSuccessListener {
                val reviews = cosplay.reviews
                reviews?.remove(cpvm.username.value!!)

                var somma = 0f

                if (reviews != null) {
                    somma = reviews.values.fold(0) { a, b -> a + b.vote!!}.toFloat()
                }
                val avg: String = if ((reviews?.size ?: 0) <= 1) {
                    "$somma.0"
                } else {
                    "%.1f".format(somma / (reviews?.size!!))
                }
                cpDB.child(cosplay.cosplayName)
                    .child("avgReviews")
                    .setValue(avg)
            }
    }

    fun saveAvarageAdd(cpvm: ClassPlayViewModel, cosplay: Cosplay, i: Int, cpDB: DatabaseReference) {
        val review = Review(cpvm.username.value)
        review.vote = i
        var reviews = cosplay.reviews

        if (reviews == null) {
            reviews = mutableMapOf<String, Review>()
        }

        reviews[cpvm.username.value!!] = review

        cpDB
            .child(cosplay.cosplayName!!)
            .child("reviews")
            .setValue(reviews)
            .addOnSuccessListener {
                val somma = reviews.values.fold(0) { a, b -> a + b.vote!!}.toFloat()

                val avg: String = if (reviews.size <= 1) {
                    "$somma.0"
                } else {
                    "%.1f".format(somma / (reviews.size))
                }
                cpDB
                    .child(cosplay.cosplayName)
                    .child("avgReviews")
                    .setValue(avg)
            }
    }
}