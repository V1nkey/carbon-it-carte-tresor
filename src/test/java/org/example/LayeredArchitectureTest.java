package org.example;

import static com.tngtech.archunit.core.domain.JavaModifier.PUBLIC;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.List;

@AnalyzeClasses(
        packages = "org.example",
        importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
public class LayeredArchitectureTest {

    private static final String ROOT_PATH = "org.example";
    private static final String APPLICATION_NAME = "application";
    private static final String APPLICATION_PACKAGE = ROOT_PATH + ".application";
    private static final String APPLICATION_PATH = ROOT_PATH + ".application..";
    private static final String DOMAIN_NAME = "domain";
    private static final String DOMAIN_PATH = ROOT_PATH + ".domain..";
    private static final String MODEL_NAME = "model";
    private static final String MODEL_PATH = ROOT_PATH + ".domain.model..";
    private static final String USE_CASES_NAME = "usecases";
    private static final String USE_CASES_PATH = ROOT_PATH + ".domain.usecases..";
    private static final String INFRASTRUCTURE_NAME = "infrastructure";
    private static final String INFRASTRUCTURE_PATH = ROOT_PATH + ".infrastructure..";

    @ArchTest
    public static final ArchRule clean_architecure_layers_are_respected =
            layeredArchitecture()
                    .consideringOnlyDependenciesInLayers()
                    .layer(APPLICATION_NAME)
                    .definedBy(APPLICATION_PATH)
                    .layer(DOMAIN_NAME)
                    .definedBy(DOMAIN_PATH)
                    .layer(USE_CASES_NAME)
                    .definedBy(USE_CASES_PATH)
                    .layer(MODEL_NAME)
                    .definedBy(MODEL_PATH)
                    .layer(INFRASTRUCTURE_NAME)
                    .definedBy(INFRASTRUCTURE_PATH)
                    .whereLayer(APPLICATION_NAME)
                    .mayNotBeAccessedByAnyLayer()
                    .whereLayer(DOMAIN_NAME)
                    .mayOnlyBeAccessedByLayers(APPLICATION_NAME, INFRASTRUCTURE_NAME)
                    .whereLayer(MODEL_NAME)
                    .mayOnlyBeAccessedByLayers(
                            APPLICATION_NAME, INFRASTRUCTURE_NAME, USE_CASES_NAME)
                    .whereLayer(USE_CASES_NAME)
                    .mayOnlyBeAccessedByLayers(APPLICATION_NAME, INFRASTRUCTURE_NAME)
                    .whereLayer(INFRASTRUCTURE_NAME)
                    .mayOnlyBeAccessedByLayers(APPLICATION_NAME);

    @ArchTest
    public static final ArchRule
            class_in_use_case_layer_should_have_only_one_public_method_called_execute =
                    classes()
                            .that()
                            .resideInAPackage(USE_CASES_PATH)
                            .and()
                            .resideOutsideOfPackage("..interactors..")
                            .and()
                            .resideOutsideOfPackage("..shared..")
                            .and()
                            .areNotInterfaces()
                            .should(
                                    new ArchCondition<>(
                                            "should have only one public method called execute") {

                                        @Override
                                        public void check(JavaClass item, ConditionEvents events) {
                                            List<JavaMethod> publicMethods =
                                                    item.getMethods().stream()
                                                            .filter(
                                                                    javaMethod ->
                                                                            javaMethod
                                                                                    .getModifiers()
                                                                                    .contains(
                                                                                            PUBLIC))
                                                            .toList();
                                            int numberOfPublicMethods = publicMethods.size();
                                            String publicMethodErrorMessage =
                                                    String.format(
                                                            "Class '%s' has more than one public"
                                                                    + " method.",
                                                            item.getFullName());
                                            events.add(
                                                    new SimpleConditionEvent(
                                                            item,
                                                            numberOfPublicMethods <= 1,
                                                            publicMethodErrorMessage));

                                            boolean isPublicMethodCalledExecute =
                                                    publicMethods.stream()
                                                            .allMatch(
                                                                    javaMethod ->
                                                                            "execute"
                                                                                    .equals(
                                                                                            javaMethod
                                                                                                    .getName()));
                                            String messageExecuter =
                                                    String.format(
                                                            "The public method of class '%s' is not"
                                                                    + " called 'execute'.",
                                                            item.getFullName());
                                            events.add(
                                                    new SimpleConditionEvent(
                                                            item,
                                                            isPublicMethodCalledExecute,
                                                            messageExecuter));
                                        }
                                    });

    @ArchTest
    public static final ArchRule interactors_should_not_be_called_in_application_layer =
            classes()
                    .that()
                    .resideInAPackage(USE_CASES_PATH)
                    .and()
                    .resideInAPackage("..interactors..")
                    .should(
                            new ArchCondition<>(
                                    "should not be called by a class in the application layer") {

                                @Override
                                public void check(JavaClass item, ConditionEvents events) {
                                    boolean isNotCalledInApplicationLayer =
                                            item.getDirectDependenciesToSelf().stream()
                                                    .noneMatch(
                                                            javaAccess ->
                                                                    javaAccess
                                                                            .getOriginClass()
                                                                            .getPackage()
                                                                            .getName()
                                                                            .startsWith(
                                                                                    APPLICATION_PACKAGE));
                                    String message =
                                            String.format(
                                                    "Interactor '%s' is called in the application"
                                                            + " layer",
                                                    item.getFullName());
                                    events.add(
                                            new SimpleConditionEvent(
                                                    item, isNotCalledInApplicationLayer, message));
                                }
                            });

    @ArchTest
    public static final ArchRule model_layer_should_not_have_any_dependency_to_use_cases_layer =
            noClasses()
                    .that()
                    .resideInAPackage(MODEL_PATH)
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage(USE_CASES_PATH);

    @ArchTest
    public static final ArchRule interactors_should_only_be_called_in_their_parent_package =
            noClasses()
                    .that()
                    .resideInAPackage(USE_CASES_PATH)
                    .and()
                    .resideInAPackage("..interactors..")
                    .and()
                    .resideOutsideOfPackage("..shared..")
                    .should(
                            new ArchCondition<>("not be called outside its parent package") {
                                @Override
                                public void check(JavaClass interactor, ConditionEvents events) {
                                    boolean isCalledOutsideParentPackage =
                                            interactor.getDirectDependenciesToSelf().stream()
                                                    .anyMatch(
                                                            classCallingInteractor -> {
                                                                String interactorPackageName =
                                                                        interactor
                                                                                .getPackage()
                                                                                .getName();
                                                                String classPackageName =
                                                                        classCallingInteractor
                                                                                .getOriginClass()
                                                                                .getPackage()
                                                                                .getName();
                                                                return !interactorPackageName
                                                                        .contains(classPackageName);
                                                            });

                                    String message =
                                            String.format(
                                                    "Interactor '%s' is called outside its parent"
                                                            + " package",
                                                    interactor.getFullName());
                                    events.add(
                                            new SimpleConditionEvent(
                                                    interactor,
                                                    isCalledOutsideParentPackage,
                                                    message));
                                }
                            });
}
