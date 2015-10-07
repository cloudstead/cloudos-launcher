TRANSLATIONS['en']['config_tabs'] = {
	dns: {
		tab: 'DNS',
		title: 'Configure DNS Services',
		description: 'This is the name that your cloudstead will have on the public Internet. ' +
		'Additionally, many apps for your cloudstead will require their own hostname. ' +
		'Configuring DNS services here will allow your cloudstead to create DNS records for these ' +
		'new hostnames when you install the apps.'
	},

	ssl: {
		tab: 'SSL',
		title: 'Set up SSL certificate',
		description: 'Your cloudstead needs a wildcard SSL certificate to enable HTTPS. ' +
		'It must be a wildcard certificate because many apps require their own hostname. ' +
		'Your cloudstead will create the hostnames in DNS, but in order for them to work with HTTPS, they must ' +
		'use this wildcard certificate. In the future, we may support per-application SSL certificates.',
		files: {
			cert_key: {
				label: 'SSL Certificate Key',
				info: 'This is the private key portion of your SSL certificate. It must be a wildcard certificate.'
			},
			cert_pem: {
				label: 'SSL Certificate PEM',
				info: 'This is the public key portion of your SSL certificate. It must be a wildcard certificate.'
			}
		}
	},

	smtp: {
		tab: 'Email',
		title: 'Setup email delivery services',
		description: 'Your cloudstead cannot deliver email by itself. The Internet is so overrun with spam these days ' +
		'that any time a brand new server pops up on the Internet and tries to send email, most other email servers ' +
		'in the world will not trust it. They will either totally ignore it, or will pretend to respond but actually not ' +
		'relay or deliver any email. So it\'s important to have an SMTP relay set up that your cloudstead will route its ' +
		'mail through. If you run your own SMTP relay, you can enter those credentials here. If you do not have an SMTP ' +
		'relay, signing up with <a href="https://sendgrid.com">Sendgrid</a> (send up to 400/day or 12,000/month for free) ' +
		'or <a href="http://www.mailgun.com/">Mailgun</a> (send up to 10,000/month for free)'
	},

	two_factor: {
		tab: 'Two-Factor Auth',
		title: 'Configure two-factor authentication for logins to your cloudstead',
		description: 'Your cloudstead supports two-factor authentication via integration ' +
		'with <a href="https://www.authy.com/">Authy</a>. If you have an Authy account, enter your API ' +
		'key here.'
	},

	storage: {
		tab: 'Storage',
		title: 'Configure external cloud storage',
		description: 'You can enjoy unlimited data storage by leveraging a cloud storage provider. ' +
		'Cloud storage is used by certain apps (like the built-in owncloud server), and during backup/restore operations' +
		'Your cloudstead will always encrypt data locally before writing to the cloud storage. ' +
		'Currently only Amazon S3 is supported but we have plans to support many more cloud storage providers.'
	},

	geoip: {
		tab: 'GeoIP',
		title: 'Configure GeoIP Databases',
		description: 'GeoIP databases provide a means for determining, based on another computer\'s Internet address, ' +
		'where in the world it is. Apps on your cloudstead can take advantage of GeoIP services to offer enhanced features. ' +
		'Cloudstead supports GeoIP databases from MaxMind, a leading GeoIP provider. Support for additonal providers ' +
		'is on the roadmap.',
		files: {
			GeoIP: {
				label: 'GeoIP Legacy Database',
				info: 'GeoIP Database, legacy format, license required. <a href="http://dev.maxmind.com/geoip/legacy/downloadable/">Purchase from Maxmind</a>.'
			},
			GeoLiteCity: {
				label: 'GeoIP Lite City Database',
				info: 'City Database, legacy format, free. <a href="http://dev.maxmind.com/geoip/legacy/geolite/">Download from from Maxmind</a>.'
			},
			GeoIP2_City: {
				label: 'GeoIP2 City Database',
				info: 'City Database, more accurate, license required. <a href="https://www.maxmind.com/en/geoip2-city">Purchase from Maxmind</a>.'
			},
			GeoLite2_City: {
				label: 'GeoIP2 Lite City Database',
				info: 'City Database, free. <a href="http://dev.maxmind.com/geoip/geoip2/geolite2/">Download from Maxmind</a>.'
			},
			GeoLite2_Country: {
				label: 'GeoIP2 Lite Country Database',
				info: 'Country Database, free. <a href="http://dev.maxmind.com/geoip/geoip2/geolite2/">Download from Maxmind</a>.'
			}
		}
	},

	claim: {
		tab: 'Claiming',
		title: 'How to Claim Your Cloudstead',
		description: 'It takes two pieces of information to claim a cloudstead. First, you need to know the ' +
		'password, which is set here. Secondly, when the cloudstead has completed setting itself up, it ' +
		'will send an email to the address set here. That message will include the URL to claim the cloudstead ' +
		'along with a key that it generated itself. This is the only place that you can set both the password and ' +
		'the email address. Once your cloudstead is running, the primary admin user will be responsible for deciding ' +
		'who has access and how much they should have. Initially this primary admin is probably you, but ' +
		'you\'re always free to delegate :)',
		fields: {
			'admin_initial_pass': {
				label: 'Password to Unlock',
				info: 'This is the password that you\'ll use to claim the cloudstead. It will be strongly encrypted ' +
				'when it is written to the configuration files. It will never be stored in plaintext ' +
				'in any durable format.'
			}
		}
	},

	apps: {
		tab: 'Apps',
		title: 'Pre-Install Apps',
		description: 'Select the apps you would like to come pre-installed on your cloudstead',
		config: {
			configure: "Configure",
			back: "Back",
		},
	},

};
