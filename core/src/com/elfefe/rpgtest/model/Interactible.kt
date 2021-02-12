package com.elfefe.rpgtest.model

class Interactible(
        val id: Int
) {
    constructor(id: Int, interaction: () -> Unit) : this(id) {
        this.interaction = interaction
    }

    var asInteracted = false
    var interaction: () -> Unit = {}
        get() = {
            field()
            asInteracted = false
        }
}