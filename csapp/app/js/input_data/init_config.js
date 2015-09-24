INIT_CONFIG = {
    dns: {
        field_prefix: 'base/base',
        fields: [ 'hostname', 'parent_domain' ],
        sub_tabs: 'cloudos/init/dns.mode'
    },

    ssl: {
        files: {
            cert_key: 'certs/cloudos/ssl-https.key',
            cert_pem: 'certs/cloudos/ssl-https.pem'
        }
    },

    smtp: {
        field_prefix: 'email/init',
        sub_tabs: 'email/init/server_type'
    },

    two_factor: {
        fields: ['authy.user', 'authy.base_uri']
    },

    storage: {
        fields: ['aws_access_key', 'aws_secret_key', 's3_bucket', 'aws_iam_user', 'backup_cron_schedule']
    },

    geoip: {
        files: {
            GeoIP: 'geoip/GeoIP.dat.gz',
            GeoLiteCity: 'geoip/GeoLiteCity.dat.gz',
            GeoIP2_City: 'geoip/GeoIP2-City.mmdb.gz',
            GeoLite2_City: 'geoip/GeoLite2-City.mmdb.gz',
            GeoLite2_Country: 'geoip/GeoLite2-Country.mmdb.gz'
        }
    },

    claim: {
        fields: ['recovery_email', 'admin_initial_pass']
    }
};
