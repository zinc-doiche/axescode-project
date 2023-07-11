package com.github.axessystem.util

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * Config를 통한 관리자의 초기설정이 필요한 객체를 나타냅니다.
 */
interface Configurable {
    val configFile: File
    val config: YamlConfiguration?

    /**
     * 파일의 템플릿과 형식 등을 미리 설정합니다.
     */
    fun configInit()

    /**
     * 서버 로드 시 파일을 읽어와 객체를 인스턴스하거나 초기설정을 마칩니다.
     */
    fun read()

    /**
     * Config 업데이트 시 실행됩니다.
     */
    fun updateConfig()
}