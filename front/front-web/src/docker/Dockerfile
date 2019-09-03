# Copyright 2018, Steven W. Anderson
FROM nginx:1.15.0

RUN rm /var/log/nginx/*

COPY nginx /etc/nginx
COPY docroot /var/www/html/

EXPOSE 80

CMD nginx

