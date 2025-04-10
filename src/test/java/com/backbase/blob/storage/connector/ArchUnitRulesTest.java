package com.backbase.blob.storage.connector;

import com.backbase.buildingblocks.archunit.ArchitectureRules;
import com.backbase.buildingblocks.archunit.ConfigurationRules;
import com.backbase.buildingblocks.archunit.test.AllConfigurationRules;
import com.backbase.buildingblocks.archunit.test.AllControllerRules;
import com.backbase.buildingblocks.archunit.test.AllGeneralCodingRules;
import com.backbase.buildingblocks.archunit.test.AllLoggingRules;
import com.backbase.buildingblocks.archunit.test.AllNamingConventionRules;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = Application.class, importOptions = DoNotIncludeTests.class)
public class ArchUnitRulesTest {

    @ArchTest
    ArchRule layeredArchitectureShouldBeAdopted = ArchitectureRules.LAYERED_ARCHITECTURE_SHOULD_BE_ADOPTED;

    @ArchTest
    ArchRule controllerClassesShouldBeInAppropriatePackage =
        ArchitectureRules.CONTROLLER_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule serviceClassesShouldBeInAppropriatePackage =
        ArchitectureRules.SERVICE_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule mapperClassesShouldBeInAppropriatePackage =
        ArchitectureRules.MAPPER_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule configurationClassesShouldBeInAppropriatePackage =
        ArchitectureRules.CONFIGURATION_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule repositoryClassesShouldBeInAppropriatePackage =
        ArchitectureRules.REPOSITORY_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule clientClassesShouldBeInAppropriatePackage =
        ArchitectureRules.CLIENT_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule actuatorEndpointClassesShouldBeInAppropriatePackage =
        ArchitectureRules.ACTUATOR_ENDPOINT_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule eventHandlerClassesShouldBeInAppropriatePackage =
        ArchitectureRules.EVENT_HANDLER_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule auditClassesShouldBeInAppropriatePackage =
        ArchitectureRules.AUDIT_CLASSES_SHOULD_BE_IN_APPROPRIATE_PACKAGE;

    @ArchTest
    ArchRule configurationClassesShouldBeUnderBackbaseKey =
        ConfigurationRules.CONFIGURATION_CLASSES_SHOULD_BE_UNDER_BACKBASE_KEY;

    @ArchTest
    ArchRule configurationClassesShouldBeValidated = ConfigurationRules.CONFIGURATION_CLASSES_SHOULD_BE_VALIDATED;

    @ArchTest
    ArchTests allControllerRules = ArchTests.in(AllControllerRules.class);

    @ArchTest
    ArchTests allGeneralCodingRules = ArchTests.in(AllGeneralCodingRules.class);

    @ArchTest
    ArchTests allLoggingRules = ArchTests.in(AllLoggingRules.class);

    @ArchTest
    ArchTests allNamingConventionRules = ArchTests.in(AllNamingConventionRules.class);

}