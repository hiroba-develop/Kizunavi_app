package com.kizunavi.gradle

import com.kizunavi.gradle.ddl.DdlParser
import com.kizunavi.gradle.ddl.EntityJavaGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class EntityGeneratorTask extends DefaultTask {

    @InputFile
    abstract RegularFileProperty getDdlFile()

    @OutputDirectory
    abstract DirectoryProperty getOutputDir()

    @Input
    abstract Property<String> getJavaPackage()

    @TaskAction
    void generate() {
        def parser = new DdlParser()
        def tables = parser.parse(ddlFile.get().asFile)
        def generator = new EntityJavaGenerator(javaPackage.get(), tables)
        generator.generateAll(tables, outputDir.get().asFile)
        logger.lifecycle("Generated ${tables.size()} entities into ${outputDir.get().asFile}")
    }
}
