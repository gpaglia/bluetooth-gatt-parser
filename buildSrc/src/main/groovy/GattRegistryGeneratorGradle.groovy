import groovy.json.JsonBuilder
import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Path

class GattRegistryGeneratorGradle {

    static generate(final Project project, boolean overwrite) {

        final Path basedir = project.getRootDir().toPath()
        final Path builddir = project.getBuildDir().toPath()

        generate(
            basedir.resolve(Path.of("src", "main", "resources", "gatt", "characteristic")),
            builddir.resolve(Path.of("generated-sources","groovy", "gatt", "characteristic", "gatt_spec_registry.json")),
            overwrite
        )

        generate(
            basedir.resolve(Path.of("src", "main", "resources", "gatt", "service")),
            builddir.resolve(Path.of("generated-sources", "groovy", "gatt", "service", "gatt_spec_registry.json")),
            overwrite
        )

    }

    static generate(final Path inputFolder, final Path registryFile, boolean overwrite) {

        try {
            if (overwrite && Files.exists(registryFile)) {
                Files.delete(registryFile)
            }
            Files.createDirectories(registryFile.getParent())
            Files.createFile(registryFile)

            final XmlParser parser = new XmlParser()
            parser.setFeature('http://apache.org/xml/features/disallow-doctype-decl', true)
            final def registry = new HashMap()

            inputFolder.toFile().eachFileMatch(~/.*.xml/) { file ->
                def xml = parser.parse(file)
                def type = xml.attributes()['type']
                if ("${type}.xml" != file.name) {
                    throw new IllegalStateException(
                        "GATT registry generation failed. 'type' attribute ($type) does not match to its file name ($file.name)")
                }
                registry.put(xml.attributes()["uuid"], xml.attributes()["type"])
            }
            registryFile.write(new JsonBuilder(registry).toPrettyString())
        } catch (Exception e) {
            System.err.println("Got exception generating registry file, exception is " + e.toString())
            e.printStackTrace()
            throw e;
        }
    }
}