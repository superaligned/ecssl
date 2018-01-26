# The Entrecloud LetsEncrypt/ACME SSL Client

```
     ___________________________
    /                           |
   /________________________    |
                            |   |
 _____________________      |   |   _____________           __    ____________    __________      _____________       ________      __              _______       __         __    ___________
 |                   /      |   |  |             |  |\     |  |  |            |  |          \    |             |     /        \    |  |           /       \     |  |       |  |  |           \
 |   _______________/  /|   |   |  |_____________|  | \    |  |  |____    ____|  |   _____   \   |_____________|    /  ______  \   |  |          /   ___   \    |  |       |  |  |   _______  \
 |   |                / |   |   |                   |  \   |  |       |  |       |  |     \  |                     /  /      \__\  |  |         /   /   \   \   |  |       |  |  |  |       \  \
 |   |    ___        |  |   |   |                   |   \  |  |       |  |       |  |     |  |                    /  /             |  |        /   /     \   \  |  |       |  |  |  |        \  \
 |   |    |  |       |  |   |   |   _____________   |    \ |  |       |  |       |  |_____/  |    _____________   |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |
 |   |    |  |       |  |   |   |  |             |  |     \|  |       |  |       |           /   |             |  |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |
 |   |    |  |       |  |   |   |  |_____________|  |  |\     |       |  |       |  ___   __/    |_____________|  |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |
 |   |    |  |       |__|   |   |                   |  | \    |       |  |       |  |  \  \                       |  |             |  |        |  |       |  |  |  |       |  |  |  |        |  |
 |   |    |  |              |   |                   |  |  \   |       |  |       |  |   \  \                      \  \        __   |  |        \   \     /   /  \   \     /   /  |  |        /  /
 |   |    | /  _____________|   |   _____________   |  |   \  |       |  |       |  |    \  \     _____________    \  \______/  /  |  |______   \   \___/   /    \   \___/   /   |  |_______/  /
 |   |    |/  /                 |  |             |  |  |    \ |       |  |       |  |     \  \   |             |    \          /   |         |   \         /      \         /    |            /
 |   |       /__________________|  |_____________|  |__|     \|       |__|       |__|      \__\  |_____________|     \________/    |_________|    \_______/        \_______/     |___________/
 |   |
 |   |______________________
 |                         /
 |________________________/
```

## Abstract

ECSSL is a command line utility to generate certificates with LetsEncrypt, and possibly other ACME-compliant certificate
authorities. It is developed by the Wordpress Cloud Hosting Provider Entrecloud. (DUH)

## Installing

To install this software, you need to have either Oracle Java 8 or OpenJDK 8. Other than that, head over to the
[releases section](https://github.com/entrecloud/ecssl/releases) and download the latest jar file.

## Running

ECSSL currently supports only one mode, invoked with `--single`, which generates/renews a single SSL certificate. The
default configuration goes like this:

```bash
java -jar ecssl.jar \
  --mode single \
  --account-key-file /etc/ecssl/account.pem \
  --account-email you@example.com \
  --domains yourdomain.com,www.yourdomain.com \
  --certificate-file /etc/ecssl/yourdomain.com.crt \
  --chain-file /etc/ecssl/yourdomain.com.fullchain.crt \
  --privatekey-file /etc/ecssl/yourdomain.com.pem \
  --webroot-path /var/www/domain.com/htdocs
```

That's it! Now it's up to you to wrap a shell script around it to restart your webserver, or however you want to use it.

The full list of options can, of course, be show when invoking the client with the `-h` or `--help` flags.

**Note:** the agent does not write any files apart from the ones you specify in the command line options. However,
if the account key is not valid, it will automatically attempt to register a new account and overwrite the account
file. It is **strongly** recommended that you back up all your account keys and certificates.

## Standing on the Shoulders of Giants

This project would not have been possible without these open source projects:

- [ACME4j by Richard Körber](https://github.com/shred/acme4j)
- [SLF4j](https://www.slf4j.org/)
- [JSR305 by Google FindBugs](http://findbugs.sourceforge.net/)
- [JUnit](http://junit.org/)

## Future Plans

- Generate multiple certificates in one go
- Run continuously as an agent and renew certificates when needed
- Integrate with various DNS providers to provide verification
- Store certificates in a database
- API
- Web interface to manage certificates
