package entity

import java.util.UUID

/**
 * 快捷键配置
 */
data class InputConfig(var desc: String,var seq:Int = 0, var values: List<String>,val id:String = UUID.randomUUID().toString()) {

    override fun toString(): String {
        return "InputConfig(desc='$desc', seq=$seq, values=$values)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InputConfig

        if (desc != other.desc) return false
        if (seq != other.seq) return false
        return values == other.values
    }

    override fun hashCode(): Int {
        var result = desc.hashCode()
        result = 31 * result + seq
        result = 31 * result + values.hashCode()
        return result
    }

}