package com.github.zsoltk.composeribs.client.child

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.zsoltk.composeribs.core.LeafNode
import com.github.zsoltk.composeribs.ui.*

class ChildNode(
    private val i: Int
) : LeafNode() {

    private val colors = listOf(
        manatee,
        sizzling_red,
        atomic_tangerine,
        silver_sand,
        md_pink_500,
        md_indigo_500,
        md_blue_500,
        md_light_blue_500,
        md_cyan_500,
        md_teal_500,
        md_light_green_500,
        md_lime_500,
        md_amber_500,
        md_grey_500,
        md_blue_grey_500
    )

    private val color = colors.random()

    @Composable
    override fun View() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = color,
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text("Child ($i)")
            }
        }
    }
}

@Preview
@Composable
fun ChildPreview() {
    ChildNode(1).Compose()
}