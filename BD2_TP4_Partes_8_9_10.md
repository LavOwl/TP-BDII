## PARTE 8
```py
    1:
    ZADD passengers 2.5 federico 4 alejandra 3 julian 1 ivan 2 andrea 2 luciana 2.4 natalia
    -->
        (integer) 7

    2:
    ZRANGE passengers 0 -1 WITHSCORES
    -->
        1) "ivan"
        2) "1"
        3) "andrea"
        4) "2"
        5) "luciana"
        6) "2"
        7) "natalia"
        8) "2.4"
        9) "federico"
        10) "2.5"
        11) "julian"
        12) "3"
        13) "alejandra"
        14) "4"

    3:
    ZADD passengers 2.7 luciana
    -->
        (integer) 0

    4:
    ZADD passengers 5.1 silvia
    -->
        (integer) 1

    5:
    ZINCRBY passengers 2 alejandra
    -->
        "6"

    6:
    ZRANGE passengers 0 -1 WITHSCORES
    -->
        1) "ivan"
        2) "1"
        3) "andrea"
        4) "2"
        5) "natalia"
        6) "2.4"
        7) "federico"
        8) "2.5"
        9) "luciana"
        10) "2.7"
        11) "julian"
        12) "3"
        13) "silvia"
        14) "5.1"
        15) "alejandra"
        16) "6"

    7:
    ZREVRANGE passengers 0 -1 WITHSCORES
    -->
        1) "alejandra"
        2) "6"
        3) "silvia"
        4) "5.1"
        5) "julian"
        6) "3"
        7) "luciana"
        8) "2.7"
        9) "federico"
        10) "2.5"
        11) "natalia"
        12) "2.4"
        13) "andrea"
        14) "2"
        15) "ivan"
        16) "1"

    8:
    ZCARD passengers
    -->
        (integer) 8
    
    9:
    ZCOUNT passengers 2 3
    -->
        (integer) 5
    
    10:
    ZRANK passengers julian
    -->
        (integer) 5

    11:
    ZSCORE passengers andrea
    -->
        "2"

    12:
    ZPOPMIN passengers 1
    -->
        1) "ivan"
        2) "1"
    
    13:
    ZPOPMAX passengers 1
    -->
        1) "alejandra"
        2) "6"
    
    14:
    ZREM passengers silvia
    -->
        (integer) 1
```
## PARTE 9
```py
    1:
    HSET user:cronos "razon social" "cronos s.a." domicilio "47 236 La Plata" "teléfono" 2215556677
    -->
        (integer) 3
    
    2:
    HSET user:cronos mail info@cronos.com.ar
    -->
        (integer) 1

    3:
    HGETALL user:cronos
    -->
        1) "razon social"
        2) "cronos s.a."
        3) "domicilio"
        4) "47 236 La Plata"
        5) "tel\xc3\xa9fono" #Oops, creo que no le gustó la tilde
        6) "2215556677"
        7) "mail"
        8) "info@cronos.com.ar"

    4:
    HGET user:cronos mail
    -->
        "info@cronos.com.ar"

    5:
    HDEL user:cronos "teléfono"
    -->
        (integer) 1 #Listo, como si la tilde nunca hubiese existido hehe

    6:
    HLEN user:cronos
    -->
        (integer) 3
    
    7:
    HKEYS user:cronos
    -->
        1) "razon social"
        2) "domicilio"
        3) "mail"

    8:
    HEXISTS user:cronos cuil
    -->
        (integer) 0
    
    9:
    HVALS user:cronos
    -->
        1) "cronos s.a."
        2) "47 236 La Plata"
        3) "info@cronos.com.ar"

    10:
    HSTRLEN user:cronos mail
    -->
        (integer) 18
```
## PARTE 10
```py
    1:
    GEOADD cities -34.61315 -58.37723 "Buenos Aires" -31.4135 -64.18105 Córdoba -32.94682 -60.63932 Rosario -32.89084 -68.82717 Mendoza -26.82414 -65.2226 "San Miguel de Tucumán" -34.92145 -57.95453 "La Plata" -38.00042 -57.5562 "Mar del Plata" -24.7859 -65.41166 Salta -31.64881 -60.70868 "Santa Fe" -31.5375 -68.53639 "San Juan" -27.46056 -58.98389 Resistencia -27.79511 -64.26149 "Santiago del Estero" -27.36708 -55.89608 Posadas -24.19457 -65.29712 "San Salvador de Jujuy" -38.71959 -62.27243 "Bahía Blanca" -31.73271 -60.52897 Paraná
    -->
        (integer) 16

    2:
    ZRANGE cities 0 -1
    -->
        1) "Mendoza"
        2) "San Juan"
        3) "C\xc3\xb3rdoba"
        4) "San Miguel de Tucum\xc3\xa1n"
        5) "Santiago del Estero"
        6) "Salta"
        7) "San Salvador de Jujuy"
        8) "Bah\xc3\xada Blanca"
        9) "Mar del Plata"
        10) "Buenos Aires"
        11) "La Plata"
        12) "Rosario"
        13) "Santa Fe"
        14) "Paran\xc3\xa1"
        15) "Resistencia"
        16) "Posadas"

    3:
    GEOPOS cities "Santa Fe"
    -->
        1) 1) "-31.648808419704437"
        2) "-60.708679386811866"

    4:
    GEODIST cities "Buenos Aires" "Córdoba" KM
    -->
        "667.5963"

    5:
    GEOSEARCH cities FROMLONLAT -27.37 -55.9 BYRADIUS 100 KM
    -->
        1) "Posadas"
    
    6:
    GEOSEARCH cities FROMMEMBER "Córdoba" BYRADIUS 700 KM
    -->
        1) "Mendoza"
        2) "San Juan"
        3) "C\xc3\xb3rdoba"
        4) "San Miguel de Tucum\xc3\xa1n"
        5) "Santiago del Estero"
        6) "Salta"
        7) "San Salvador de Jujuy"
        8) "Bah\xc3\xada Blanca"
        9) "Buenos Aires"
        10) "Rosario"
        11) "Santa Fe"
        12) "Paran\xc3\xa1"
        13) "Resistencia"
```