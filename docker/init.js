db = db.getSiblingDB('admin');

db.auth("root", "root");
// log as root admin if you decided to authenticate in your docker-compose file...
db = db.getSiblingDB('statisticdb');
// create and move to your new database
db.createUser({
    'user': "user",
    'pwd': "test",
    'roles': [{
        'role': 'dbOwner',
        'db': 'statisticdb'}]});
// user created
db.createCollection('users');
db.createCollection('booking_users');
db.createCollection('booking_reservations');
// add new collection

db.users.insert({ username: 'admin', password: '$2a$12$iJYSkRAzCmyOClMwWULLYeIofo8OQzkkUfGcTlzltTjf8cP7Dc4fS', roles : ['ROLE_ADMIN']
    , firstName: 'Admin',  lastName: 'Admin', email: 'admin@admin.ru', enable: true})
