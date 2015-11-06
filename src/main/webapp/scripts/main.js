requirejs.config({
    baseUrl: 'bower_components',
    shim: {
        'facebook' : {
            exports: 'FB'
        }
    },
    paths: {
        app: '../app',
        'facebook': '//connect.facebook.net/en_US/sdk'
    }
});
requirejs(['app/main']);
