###Test de configuration: SpringConfigurationTest
On verifie que Spring MVC a besoin d'une configuration des proxies pour les injections de type request.

####SpringConfigurationTest.throwExceptionWithNoProxyRequestParameter
Ce test montre que sans proxy pour une injection de type request, Spring bloque son lancement.

####SpringConfigurationTest.throwExceptionWithNoProxyRequestParameter
Ce test lane une instance Spring, on teste la factory de bean, on voit, via le nom de la classe, que l'injection de type request est un objet construit par cglib.

###Test de requête HTTP : SpringWebRequestTest

####SpringWebRequestTest.verifyRequestInjectionIsAnonymousClass
On verifie que l'injection de type request est un objet construit par cglib.

####SpringWebRequestTest.verifyBuildNewInstanceOfRequestInjectionForEachCall
On verifie que l'injection est une nouvelle instance d'objet.