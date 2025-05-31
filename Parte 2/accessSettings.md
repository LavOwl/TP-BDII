```coffee
    mongosh
    use bd2_grupo_19
    db.createUser({user: "spring_user", pwd:"dev_pass", roles: [{role:"readWrite", db:"bd2_grupo_19"}]})
```