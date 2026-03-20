locale {
    language = 'en'
    country = 'CA'
}

timezone {
    region = 'America/Vancouver'
}

database {
    url = 'jdbc:sqlite:data/spear.sqlite'
    drop = false
    create = false
    load = false
    user = false
}

// Spear system user
spearUser {
    username = 'frodo@theshire.org'
    password = 'B@gg1ns5'
    firstName = 'Frodo'
    lastName = 'Baggins'
    roleName = 'system_admin'
}
