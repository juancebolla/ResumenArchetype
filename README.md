# baseServicioBICE
Proyecto Base para servicios BICE.

## Comandos proyecto.
* Construir proyecto.
```
mvn clean package -DskipTests
```

* Ejecutar pruebas unitarias
```
mvn surefire:test
```

* Ejecutar revisión de cobertura de código
```
mvn verify
```

* Ejecutar Sonar
```
mvn sonar:sonar -Dmaven.javadoc.skip=true -DskipTests -Dsonar.host.url=[URL_SONAR]
```
> NOTA: Se debe tener configurado la url de sonar en el equipo que se ejecuta este comando.
