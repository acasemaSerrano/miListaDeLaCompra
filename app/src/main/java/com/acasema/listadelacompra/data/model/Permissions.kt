package com.acasema.listadelacompra.data.model

import java.io.Serializable


data class Permissions(val tosomeone: String, val owner: String, var permissions: PermissionsType, val shopingList: String): Serializable {


    companion object{
        fun transformHashMapAnPermissions(PermissionsInHashMap : HashMap<*, *>) : Permissions{

            val tosomeone = PermissionsInHashMap["tosomeone"] as String
            val owner = PermissionsInHashMap["owner"] as String
            val permissions = PermissionsType.valueOf(PermissionsInHashMap["permissions"] as String)
            val shopingList = PermissionsInHashMap["shopingList"] as String

            return Permissions(tosomeone, owner, permissions, shopingList)
        }
    }


    enum class PermissionsType: Serializable {
        observer,
        buyer,
        editor
    }
}