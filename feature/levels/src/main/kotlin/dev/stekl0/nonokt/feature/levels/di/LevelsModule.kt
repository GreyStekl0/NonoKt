package dev.stekl0.nonokt.feature.levels.di

import dev.stekl0.nonokt.core.data.di.DataModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DataModule::class])
@ComponentScan("dev.stekl0.nonokt.feature.levels")
public class LevelsModule
