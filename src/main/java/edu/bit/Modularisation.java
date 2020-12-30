package edu.bit;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.spi.ResourceBundleProvider;
import java.util.stream.Collectors;

public class Modularisation {

    public void providersWithinModules() {
        ModuleFinder finder = ModuleFinder.of(Paths.get("/Users/naman.nigam/GitHub/Naman/Jigsaw/out/production/"));
        ModuleLayer parent = ModuleLayer.boot();
        Configuration cf = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("one"));
        ClassLoader scl = ClassLoader.getSystemClassLoader();
        ModuleLayer layer = parent.defineModulesWithManyLoaders(cf, scl);

        layer.modules().stream().map(Module::getDescriptor).map(ModuleDescriptor::requires)
                .forEach(System.out::println);

        ServiceLoader<SomeProvider> someProviders = ServiceLoader.load(SomeProvider.class, scl);
        ServiceLoader<SomeProvider> someProviders1 =
                ServiceLoader.load(SomeProvider.class, ClassLoader.getPlatformClassLoader());

        SomeProvider someProvider = someProviders.iterator().next();
        ServiceLoader<AnotherProvider> anotherProviders = ServiceLoader.load(ModuleLayer.boot(), AnotherProvider.class);
        AnotherProvider anotherProvider = anotherProviders.iterator().next();
        System.out.println("Cool");
    }

    public void listAllServicesOfModules() {
        ModuleLayer.boot().modules().stream().map(Module::getDescriptor).filter(md -> !md.provides().isEmpty()).sorted()
                .forEach(md -> System.out.format("%s -> %s%n", md.name(), md.provides()));
    }

    public void loadingClassFromModuleLayers() throws ClassNotFoundException {
        Path path = Paths.get("/Users/naman.nigam/GitHub/Naman/Jigsaw/out/production");

        ModuleFinder finder = ModuleFinder.of(path);

        ModuleLayer parent = ModuleLayer.boot();

        Configuration configuration = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("modular"));

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        ModuleLayer layer = parent.defineModulesWithOneLoader(configuration, systemClassLoader);

        System.out.println(layer.boot().modules().stream().map(Module::getName).collect(Collectors.toList()));
        System.out.println(layer.boot().modules().stream().map(Module::getName).collect(Collectors.toList()).size());
        Class<?> c = layer.findLoader("modular").loadClass("com.module.ModularExperiment");

        System.out.println(Arrays.toString(c.getDeclaredFields()));
        System.out.println(Arrays.toString(c.getConstructors()));
    }

    public void descriptorBuilder() {
        ModuleDescriptor descriptor = ModuleDescriptor.newModule("stats.core")
                .requires("java.base")
                .exports("org.acme.stats.core.clustering")
                .exports("org.acme.stats.core.regression")
                .packages(Set.of("org.acme.stats.core.internal"))
                .build();
    }

    public void readModuleDescriptors() throws IOException {
        ModuleDescriptor moduleDescriptor = ModuleDescriptor.read(new FileInputStream(
                "/Users/naman.nigam/GitHub/Naman/Jigsaw/out/production/standalone/module-info.class"));
        if (moduleDescriptor != null) {
            System.out.println("Module Description\n-------------------------");
            System.out.println("Requires: " + moduleDescriptor.requires());
            System.out.println("Exports: " + moduleDescriptor.exports());
            System.out.println("Uses: " + moduleDescriptor.uses());
            System.out.println("Provides: " + moduleDescriptor.provides());
            System.out.println("Packages: " + moduleDescriptor.packages());
        }
    }

    public void completeModuleGraph() {
        Path path = Paths.get("/Users/naman.nigam/GitHub/Naman/Jigsaw/out/production");
        ModuleFinder finder = ModuleFinder.of(path);
        ModuleLayer parent = ModuleLayer.boot();
        Configuration configuration = parent.configuration().resolve(finder, ModuleFinder.of(), Set.of("modular"));
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ModuleLayer layer = parent.defineModulesWithOneLoader(configuration, systemClassLoader);
        Module m = ModuleLayer.boot().findModule("my.module").orElse(null);

        Class<?> c = null;
        try {
            c = layer.findLoader("modular").loadClass("com.module.ModularExperiment");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Module module = c.getModule();

        System.out.println(module.getDescriptor().toString());
        Set<ModuleDescriptor.Requires> requires = module.getDescriptor().requires();
        System.out.println("Requires: " + requires);
        requires.forEach(s -> s.modifiers().forEach(mod -> System.out.println(s + " " + mod.toString())));

        Set<ModuleDescriptor.Exports> exports = module.getDescriptor().exports();
        System.out.println("Exports: " + exports);
        exports.forEach(s -> s.modifiers().forEach(mod -> System.out.println(mod.toString())));

        Set<ModuleDescriptor.Opens> opens = module.getDescriptor().opens();
        System.out.println("Opens: " + opens);
        opens.forEach(s -> s.modifiers().forEach(mod -> System.out.println(mod.name())));

        Set<ModuleDescriptor.Provides> provides = module.getDescriptor().provides();
        provides.forEach(p -> {
            System.out.println("service - " + p.service());
            System.out.println("providers - " + p.providers());
        });

        Set<ModuleDescriptor.Modifier> modifiers = module.getDescriptor().modifiers();
        System.out.println("Modifiers: " + modifiers);
        modifiers.forEach(mod -> System.out.println(mod.name()));

        String mainClass = module.getDescriptor().mainClass().orElse("None");
        System.out.println("Main Class: " + mainClass);

        boolean automaticModule = module.getDescriptor().isAutomatic();
        System.out.println("Is Automatic: " + automaticModule);

        boolean openModule = module.getDescriptor().isOpen();
        System.out.println("Is Open: " + openModule);

        Set<String> modulePackages = module.getDescriptor().packages();
        System.out.println("Packages: " + modulePackages);

        String moduleName = module.getDescriptor().name();
        System.out.println("Module Name: " + moduleName);

        String rawVersion = module.getDescriptor().rawVersion().orElse("0.0.0");
        System.out.println("Module Version: " + rawVersion);

        String nameAndVersion = module.getDescriptor().toNameAndVersion();
        System.out.println("Module NameAndVersion: " + nameAndVersion);
    }

    public void readModuleDescriptorFromAString() {
        String moduleInfo = "module jdkudpates {\n" +
                "    requires java.sql;\n" +
                "    requires java.net.http;\n" +
                "}";
        ModuleDescriptor moduleDescriptor = ModuleDescriptor.read(ByteBuffer.wrap(moduleInfo.getBytes()));
    }

    public interface AnotherProvider extends ModuleFinder {
    }

    public interface SomeProvider extends ResourceBundleProvider {
    }

    public class AnotherProviderImpl implements AnotherProvider {
        @Override
        public Optional<ModuleReference> find(String name) {
            return Optional.empty();
        }

        @Override
        public Set<ModuleReference> findAll() {
            return null;
        }
    }

    public class SomeProviderImpl implements SomeProvider {
        @Override
        public ResourceBundle getBundle(String baseName, Locale locale) {
            return new ResourceBundle() {
                @Override
                protected Object handleGetObject(String key) {
                    return null;
                }

                @Override
                public Enumeration<String> getKeys() {
                    return null;
                }
            };
        }
    }

    // get a module name from the jar file programmatically
    public static void extractModuleNameProgrammatically(Path dir1) {
        ModuleFinder finder = ModuleFinder.of(dir1);
        Set<ModuleReference> moduleReferences = finder.findAll();
        Set<String> moduleNames = moduleReferences.stream()
                .map(mr -> mr.descriptor().name())
                .collect(Collectors.toSet());
        System.out.println(moduleNames);
    }
}
