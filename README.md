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
authorities. It is developed by the [Wordpress Cloud Hosting Provider Entrecloud](https://entrecloud.com). If you don't
want to deal with any of this, maybe you're interested in a hosting plan? :)

## Installing

To install this software, you need to have either Oracle Java 8 or OpenJDK 8. Other than that, head over to the
[releases section](https://github.com/entrecloud/ecssl/releases) and download the latest jar file.

## Running

ECSSL currently supports only one mode, invoked with `--mode single`, which generates/renews a single SSL certificate.
The default configuration goes like this:

```bash
java -jar ecssl.jar \
  --mode single \
  --account-key-file /etc/ecssl/account.pem \
  --account-email you@example.com \
  --domains yourdomain.com,www.yourdomain.com \
  --certificate-file /etc/ecssl/yourdomain.com.crt \
  --chain-file /etc/ecssl/yourdomain.com.fullchain.crt \
  --privatekey-file /etc/ecssl/yourdomain.com.pem \
  --webroot-path /var/www/domain.com/htdocs \
  --agree-terms-url https://letsencrypt.org/documents/LE-SA-v1.2-November-15-2017.pdf
```

That's it! Now it's up to you to wrap a shell script around it to restart your webserver, or however you want to use it.
No root access needed, no configuration files that can break, just a single executable.

The full list of options can, of course, be show when invoking the client with the `-h` or `--help` flags.

**Note:** the agent does not write any files apart from the ones you specify in the command line options. However,
if the account key is not valid, it will automatically attempt to register a new account and overwrite the account
file. It is **strongly** recommended that you back up all your account keys and certificates.

## FAQ

### Do I need to run it as root?

No! In fact, I wouldn't recommend running it as root. The program can run as a normal user, assuming that it can access
and replace the following files:

- Your account key, domain private key and certificate files.
- The `.well-known/acme-challenge` directory in your webroot.

### How do I make it less verbose?

Simply pass `--log-level warn` or `--log-level error` as a parameter. Conversely, you can make it more verbose by
specifying `--log-level trace`, which creates a LOT of output.

### Does it automatically configure my Apache/nginx/etc?

No. This is just an ACME client, it doesn't touch your webserver.

### Why Java? Can you write it in Go/Python/something else?

This is an open source version of a tool that we built internally, and internally we use a lot of Java, so sorry,
no implementations in other languages. However, you may wish to look around, there are a ton of ACME clients out there.

### Can I run it with Java 7?

In short? No.

Right now the code is Java 7 compatible, but it won't stay this way. In the long run, this codebase will receive
additional features that will no longer be Java 7 compatible, therefore we won't be supporting Java 7 at all.

## Standing on the Shoulders of Giants

This project would not have been possible without [ACME4j by Richard Körber](https://github.com/shred/acme4j).

## Future Plans

- Generate multiple certificates in one go
- Run continuously as an agent and renew certificates when needed
- Integrate with various DNS providers to provide verification
- Store certificates in a database
- API
- Web interface to manage certificates
