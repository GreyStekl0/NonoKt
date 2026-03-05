package dev.stekl0.nonokt.feature.levels.impl.di

import dev.stekl0.nonokt.feature.levels.impl.LevelsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

public val levelsFeatureModule: Module =
    module {
        viewModelOf(::LevelsViewModel)
    }
