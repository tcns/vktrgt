// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '../../',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'main/webapp/bower_components/jquery/dist/jquery.js',
            'main/webapp/bower_components/angular/angular.js',
            'main/webapp/bower_components/angular-aria/angular-aria.js',
            'main/webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'main/webapp/bower_components/angular-cache-buster/angular-cache-buster.js',
            'main/webapp/bower_components/angular-cookies/angular-cookies.js',
            'main/webapp/bower_components/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'main/webapp/bower_components/angular-local-storage/dist/angular-local-storage.js',
            'main/webapp/bower_components/angular-loading-bar/build/loading-bar.js',
            'main/webapp/bower_components/angular-resource/angular-resource.js',
            'main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'main/webapp/bower_components/angular-translate/angular-translate.js',
            'main/webapp/bower_components/messageformat/messageformat.js',
            'main/webapp/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
            'main/webapp/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'main/webapp/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
            'main/webapp/bower_components/bootstrap-sass/assets/javascripts/bootstrap.js',
            'main/webapp/bower_components/bootstrap/dist/js/bootstrap.js',
            'main/webapp/bower_components/json3/lib/json3.js',
            'main/webapp/bower_components/ng-file-upload/ng-file-upload.js',
            'main/webapp/bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'main/webapp/bower_components/sockjs-client/dist/sockjs.js',
            'main/webapp/bower_components/stomp-websocket/lib/stomp.min.js',
            'main/webapp/bower_components/fastclick/lib/fastclick.js',
            'main/webapp/bower_components/nprogress/nprogress.js',
            'main/webapp/bower_components/Chart.js/dist/Chart.js',
            'main/webapp/bower_components/gauge.js/dist/gauge.js',
            'main/webapp/bower_components/gauge.js/dist/gauge.min.js',
            'main/webapp/bower_components/bootstrap-progressbar/bootstrap-progressbar.js',
            'main/webapp/bower_components/iCheck/icheck.min.js',
            'main/webapp/bower_components/Flot/jquery.flot.js',
            'main/webapp/bower_components/moment/moment.js',
            'main/webapp/bower_components/bootstrap-daterangepicker/daterangepicker.js',
            'main/webapp/bower_components/jquery-sparkline/dist/jquery.sparkline.js',
            'main/webapp/bower_components/eve/eve.js',
            'main/webapp/bower_components/raphael/raphael.min.js',
            'main/webapp/bower_components/mocha/mocha.js',
            'main/webapp/bower_components/morris.js/morris.js',
            'main/webapp/bower_components/jquery.hotkeys/jquery.hotkeys.js',
            'main/webapp/bower_components/google-code-prettify/bin/prettify.min.js',
            'main/webapp/bower_components/bootstrap-wysiwyg/js/bootstrap-wysiwyg.min.js',
            'main/webapp/bower_components/jquery.tagsinput/src/jquery.tagsinput.js',
            'main/webapp/bower_components/select2/dist/js/select2.js',
            'main/webapp/bower_components/autosize/dist/autosize.js',
            'main/webapp/bower_components/parsleyjs/dist/parsley.js',
            'main/webapp/bower_components/devbridge-autocomplete/dist/jquery.autocomplete.js',
            'main/webapp/bower_components/ion.rangeSlider/js/ion.rangeSlider.js',
            'main/webapp/bower_components/mjolnic-bootstrap-colorpicker/dist/js/bootstrap-colorpicker.js',
            'main/webapp/bower_components/jquery.inputmask/dist/inputmask/inputmask.js',
            'main/webapp/bower_components/jquery-knob/js/jquery.knob.js',
            'main/webapp/bower_components/cropper/dist/cropper.js',
            'main/webapp/bower_components/dropzone/dist/min/dropzone.min.js',
            'main/webapp/bower_components/fullcalendar/dist/fullcalendar.js',
            'main/webapp/bower_components/requirejs/require.js',
            'main/webapp/bower_components/jquery.easy-pie-chart/dist/jquery.easypiechart.js',
            'main/webapp/bower_components/starrr/dist/starrr.js',
            'main/webapp/bower_components/datatables.net/js/jquery.dataTables.js',
            'main/webapp/bower_components/datatables.net-bs/js/dataTables.bootstrap.js',
            'main/webapp/bower_components/datatables.net-buttons/js/dataTables.buttons.js',
            'main/webapp/bower_components/datatables.net-buttons/js/buttons.colVis.js',
            'main/webapp/bower_components/datatables.net-buttons/js/buttons.flash.js',
            'main/webapp/bower_components/datatables.net-buttons/js/buttons.html5.js',
            'main/webapp/bower_components/datatables.net-buttons/js/buttons.print.js',
            'main/webapp/bower_components/datatables.net-buttons-bs/js/buttons.bootstrap.js',
            'main/webapp/bower_components/datatables.net-fixedheader/js/dataTables.fixedHeader.js',
            'main/webapp/bower_components/datatables.net-keytable/js/dataTables.keyTable.js',
            'main/webapp/bower_components/datatables.net-responsive/js/dataTables.responsive.js',
            'main/webapp/bower_components/datatables.net-responsive-bs/js/responsive.bootstrap.js',
            'main/webapp/bower_components/datatables.net-scroller/js/dataTables.scroller.js',
            'main/webapp/bower_components/pdfmake/build/pdfmake.js',
            'main/webapp/bower_components/pdfmake/build/vfs_fonts.js',
            'main/webapp/bower_components/jszip/dist/jszip.js',
            'main/webapp/bower_components/angular-mocks/angular-mocks.js',
            // endbower
            'main/webapp/scripts/app/app.js',
            'main/webapp/scripts/app/**/*.js',
            'main/webapp/scripts/components/**/*.+(js|html)',
            'test/javascript/spec/helpers/module.js',
            'test/javascript/spec/helpers/httpBackend.js',
            'test/javascript/**/!(karma.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: [],

        preprocessors: {
            './**/*.js': ['coverage']
        },

        reporters: ['dots', 'jenkins', 'coverage', 'progress'],

        jenkinsReporter: {

            outputFile: '../build/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {

            dir: '../build/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['Chrome'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout : 10000, // default 2000
        browserDisconnectTolerance : 1, // default 0
        browserNoActivityTimeout : 4*60*1000 //default 10000
    });
};
