function fn() {
    var config = {
        baseUrl: 'http://localhost:8081'
    };
    karate.log('Karate config:', config);
    return config;
}