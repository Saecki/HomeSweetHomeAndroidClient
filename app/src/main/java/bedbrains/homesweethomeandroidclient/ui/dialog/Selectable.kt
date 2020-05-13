package bedbrains.homesweethomeandroidclient.ui.dialog

data class Selectable<V>(var text: String, var value: V, var selected: Boolean, var callback: (String, V) -> Unit)