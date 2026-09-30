# 1. Identificación y formulación del problema

## 1.1 Situación problemática

La planificación y el seguimiento de las actividades académicas requieren organizar diferentes responsabilidades, como los horarios de clases, las fechas de entrega, los proyectos, las tareas diarias y la asistencia. Cuando esta información se encuentra distribuida en diferentes medios o no se actualiza de manera organizada, puede resultar más difícil identificar las actividades pendientes, establecer prioridades y conocer el avance de los compromisos académicos.

En este contexto, el estudiante es el principal usuario que necesita organizar su ciclo académico, planificar sus actividades semanales, registrar sus avances y controlar el cumplimiento de sus responsabilidades. Asimismo, los familiares que participan en su acompañamiento pueden necesitar información sobre su progreso, sus actividades pendientes y las situaciones que requieren atención, sin intervenir directamente en su planificación.

El proyecto plantea un sistema que reúne estas funciones en un mismo entorno digital. El HTML contempla módulos de preparación del ciclo, cursos y horarios, agenda académica, organización semanal, proyectos académicos, trabajo diario, asistencia a clases, supervisión semanal y tableros de control. También considera diferentes perfiles de acceso, incluyendo el estudiante, la supervisora familiar y los perfiles de consulta y administración.

Sin embargo, la existencia de estas funcionalidades en el diseño no permite afirmar todavía que los usuarios tengan dificultades concretas con sus métodos actuales. Para sustentar la situación problemática, se deberá observar cómo se organizan actualmente las actividades académicas, qué herramientas utilizan y qué dificultades encuentran al planificar y revisar sus avances.

## 1.2 Formulación

¿Cómo se puede mejorar la planificación y el seguimiento de las actividades académicas de un estudiante, considerando la organización de sus compromisos, el registro de sus avances y la supervisión de su progreso?

Esta pregunta permite estudiar el problema sin dar por hecho que una aplicación específica es la única solución. El desarrollo del sistema será la propuesta que se evaluará durante el proyecto.

## 1.3 Diagrama de Ishikawa

Problema central:

> Dificultad para mantener una planificación y un seguimiento académico organizados e integrados.

Las siguientes son categorías y posibles causas para investigar. No deben presentarse como causas confirmadas hasta que tengan evidencia.

|
Categoría

|

Posibles causas

|
| --- | --- |
|

Métodos

|

Falta de una rutina de planificación semanal; ausencia de un procedimiento uniforme para revisar pendientes.

|
|

Información

|

Fechas de entrega, horarios y tareas posiblemente distribuidos en distintos medios; información que podría quedar desactualizada.

|
|

Tecnología

|

Herramientas que no integran la planificación, el registro de avances y la supervisión en un solo espacio.

|
|

Seguimiento

|

Falta de un registro continuo del progreso; dificultad para identificar compromisos atrasados o pendientes de revisión.

|
|

Organización

|

Dificultad para priorizar actividades; responsabilidades académicas que coinciden en determinados periodos.

|
|

Comunicación

|

Posible falta de coordinación entre el estudiante y los familiares respecto al progreso y las actividades que requieren atención.

|

### Interpretación

El análisis preliminar permite plantear que las dificultades de planificación y seguimiento podrían estar relacionadas con la distribución de la información, la organización de las actividades y la falta de continuidad en el registro de los avances. También se considera la necesidad de facilitar la comunicación entre el estudiante y los familiares que participan en su acompañamiento.

Estas posibles causas guardan relación con los módulos previstos en el sistema. La agenda académica y los cursos permiten centralizar información; la organización semanal y el trabajo diario apoyan la planificación y el registro de actividades; mientras que la asistencia, la supervisión y los tableros facilitan el seguimiento.

No obstante, estas relaciones representan el enfoque funcional del proyecto, no una comprobación de las causas reales. Para completar el Ishikawa, el equipo deberá contrastar cada hipótesis con observaciones, entrevistas o revisión de los métodos actuales de organización.

## 1.1 Antecedentes

El HTML proporcionado constituye el antecedente directo del proyecto, puesto que contiene una versión previa de un sistema de planificación y seguimiento académico. Esta versión contempla una interfaz organizada por módulos, acceso mediante perfiles y funcionalidades orientadas a la preparación del ciclo, la programación de actividades, el registro de avances y la supervisión familiar.

El proyecto no parte, por tanto, de una aplicación completamente nueva, sino de una solución existente que requiere revisión, adecuación y mejora para responder a los requerimientos establecidos para la entrega final.

Como parte de los antecedentes externos, se deberán investigar herramientas de planificación académica, calendarios digitales y sistemas de seguimiento de tareas que permitan identificar funcionalidades similares, sus características y las necesidades que atienden. La comparación deberá centrarse en aspectos como la organización de actividades, la gestión de fechas límite, el seguimiento del progreso y la colaboración o consulta de terceros.

Pendiente: incorporar fuentes bibliográficas externas pertinentes, con autor, año y referencia completa en APA 7. El HTML por sí solo no proporciona referencias académicas externas para sustentar este apartado.

## 1.5 Justificación

El desarrollo y la mejora del Centro de Planificación y Seguimiento Académico se justifican por la necesidad de contar con una herramienta que permita reunir y organizar las principales actividades relacionadas con el ciclo académico de un estudiante. La propuesta busca integrar en un mismo entorno la planificación, el registro de tareas, la gestión de proyectos, el control de asistencia y la supervisión del progreso.

Desde el punto de vista funcional, el sistema busca facilitar la consulta de compromisos y la identificación de actividades pendientes, además de proporcionar un espacio para registrar avances y revisar el cumplimiento de las actividades planificadas. La incorporación de diferentes perfiles permite distinguir las funciones del estudiante, la supervisión familiar y la administración, de acuerdo con los permisos previstos en el proyecto.

Desde el punto de vista tecnológico, la existencia de una versión previa en HTML permite partir de una base desarrollada y concentrar el trabajo en revisar sus funcionalidades, corregir aspectos que no respondan a los requerimientos y mejorar la organización y la experiencia de uso. El uso de Git y GitHub durante el desarrollo también permitirá mantener un historial de modificaciones, organizar el trabajo y documentar las contribuciones del equipo.

Se espera que la versión mejorada facilite la organización de las responsabilidades académicas y haga más accesible la información necesaria para el seguimiento. Estos beneficios deberán evaluarse mediante pruebas funcionales y la revisión de los criterios de aceptación establecidos en las historias de usuario.

# 5. Objetivos y alcance

## 5.1 Objetivo general

Mejorar el Centro de Planificación y Seguimiento Académico mediante la revisión, adecuación e implementación de sus funcionalidades, con la finalidad de facilitar la organización de las actividades académicas, el registro de avances y la supervisión del progreso del estudiante.

## 5.2 Objetivos específicos

1. Analizar la estructura, las funcionalidades y el estado actual del proyecto HTML, identificando los componentes operativos, las limitaciones y los aspectos que requieren adecuación según los requerimientos del proyecto final.

2. Diseñar e implementar las mejoras funcionales y de organización necesarias para integrar la planificación académica, la gestión de actividades, el registro de avances y el seguimiento del estudiante, de acuerdo con las historias de usuario y sus criterios de aceptación.

3. Verificar el funcionamiento de las funcionalidades mejoradas mediante pruebas basadas en las historias de usuario, documentando los resultados obtenidos, los errores identificados y los aspectos pendientes de desarrollo.

Nota sobre el alcance: estos objetivos son coherentes con el sistema descrito en el HTML. El alcance definitivo debe coincidir con las funcionalidades que realmente van a modificar y probar, no necesariamente con todos los módulos que aparecen en la interfaz.
