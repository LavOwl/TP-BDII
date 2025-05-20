#General Set Up:
```bash
    mongosh
    -->
```
```c
    Current Mongosh Log ID: 682cbaa168dd98c5076c4bcf
    Connecting to:          mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+2.5.1
    Using MongoDB:          8.0.9
    Using Mongosh:          2.5.1

    For mongosh info see: https://www.mongodb.com/docs/mongodb-shell/


    To help improve our products, anonymous usage data is collected and sent to MongoDB periodically (https://www.mongodb.com/legal/privacy-policy).
    You can opt-out by running the disableTelemetry() command.

    ------
        The server generated these startup warnings when booting
        2025-05-20T10:09:29.250-03:00: Access control is not enabled for the database. Read and write access to data and configuration is unrestricted
    ------
```
```bash
    use tours
```
```c
    switched to db tours
```

9)
I) 
```bash
    db.recorridos.insertOne({ nombre: "City Tour", precio: 200, stops: ["Diagonal Norte", "Avenida deMayo", "Plaza del Congreso" ], totalKm:5 })
    -->
    {
        acknowledged: true,
        insertedId: ObjectId('682cbf03ce69a625496c4bd0')
    }
```

II) Sí se muestran todos los atributos. También se muestra el id del objeto, que es auto generado por mongodb de forma única.
```bash
    #No existen los productos, la tabla es "recorridos", ¿Está desactualizada la consigna?
    db.recorridos.find()
    -->
    [
        {
            _id: ObjectId('682cbf03ce69a625496c4bd0'),
            nombre: 'City Tour',
            precio: 200,
            stops: [ 'Diagonal Norte', 'Avenida deMayo', 'Plaza del Congreso' ],
            totalKm: 5
        }
    ]
```
10)
```bash
    mongoimport --db tours --collection recorridos --file material_adicional_1.json --jsonArray
```
```c
    2025-05-20T15:17:43.945-0300    connected to: mongodb://localhost/
    2025-05-20T15:17:43.950-0300    14 document(s) imported successfully. 0 document(s) failed to import.
```
I) 
```bash
    db.recorridos.updateOne({nombre: "Museum Tour"}, { $set: {totalKm: 12}})
    -->
    {
        acknowledged: true,
        insertedId: null,
        matchedCount: 1,
        modifiedCount: 1,
        upsertedCount: 0
    }
```
II)
```bash
    db.recorridos.updateOne({nombre: "Delta Tour"}, { $push: {stops: "Tigre"}})
    -->
    {
        acknowledged: true,
        insertedId: null,
        matchedCount: 1,
        modifiedCount: 1,
        upsertedCount: 0
    }
```
III)
```c
    db.recorridos.updateMany({precio: { $exists: true }}, [{ $set: { precio: { $multiply: ["$precio", 1.1] } } } ] )
    -->
    {
        acknowledged: true,
        insertedId: null,
        matchedCount: 15,
        modifiedCount: 15,
        upsertedCount: 0
    }
```
IV)
```c
    db.recorridos.deleteOne({ nombre: "Temporal Route" })
    -->
    { acknowledged: true, deletedCount: 1 }
```
V)
```c
    db.recorridos.updateOne({ nombre: "Urban Exploration" }, { $set: {tags: []}} )
    -->
    {
        acknowledged: true,
        insertedId: null,
        matchedCount: 1,
        modifiedCount: 1,
        upsertedCount: 0
    }
    ---
    db.recorridos.updateOne({ nombre: "Urban Exploration" }, { $push: {tags: "Gastronomía"}} )
    -->
    {
        acknowledged: true,
        insertedId: null,
        matchedCount: 1,
        modifiedCount: 1,
        upsertedCount: 0
    }
```
11)
I)
```c
    db.recorridos.find({ nombre: "Museum Tour" })
    -->
    [
        {
            _id: ObjectId('682cc747bb118e581a521cc1'),
            nombre: 'Museum Tour',
            precio: 605,
            stops: [
            'Museo Nacional de Bellas Artes',
            'Teatro Colón',
            'Planetario',
            'Bosques de Palermo',
            'San Telmo',
            'La Boca - Caminito',
            'Recoleta',
            'El Monumental (Estadio River Plate)',
            'Av 9 de Julio'
            ],
            totalKm: 12
        }
    ]
```
II)
```c
    db.recorridos.find({ precio: { $gt: 600 } })
    -->
[
  {
    _id: ObjectId('682cc747bb118e581a521cb7'),
    nombre: 'Cultural Odyssey',
    precio: 715.0000000000001,
    stops: [
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio',
      'Plaza Italia'
    ],
    totalKm: 11
  },
  {
    _id: ObjectId('682cc747bb118e581a521cba'),
    nombre: 'Delta Tour',
    precio: 880.0000000000001,
    stops: [ 'Río de la Plata', 'Bosques de Palermo', 'Delta', 'Tigre' ],
    totalKm: 8
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbc'),
    nombre: 'Gastronomic Delight',
    precio: 770.0000000000001,
    stops: [
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'Costanera Sur'
    ],
    totalKm: 9
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc0'),
    nombre: 'Artistic Journey',
    precio: 660,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Usina del Arte',
      'Planetario',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Av 9 de Julio'
    ],
    totalKm: 15
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc1'),
    nombre: 'Museum Tour',
    precio: 605,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Planetario',
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio'
    ],
    totalKm: 12
  }
]
```
III)
```c
    db.recorridos.find({ precio: { $gt: 500 }, totalKm: { $gt: 10 }} )
    -->
    [
  {
    _id: ObjectId('682cc747bb118e581a521cb7'),
    nombre: 'Cultural Odyssey',
    precio: 715.0000000000001,
    stops: [
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio',
      'Plaza Italia'
    ],
    totalKm: 11
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbe'),
    nombre: 'Architectural Expedition',
    precio: 550,
    stops: [
      'Usina del Arte',
      'Puerto Madero',
      'Museo Nacional de Bellas Artes',
      'Museo Moderno',
      'Museo de Arte Latinoamericano',
      'Teatro Colón',
      'San Telmo',
      'Recoleta'
    ],
    totalKm: 12
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc0'),
    nombre: 'Artistic Journey',
    precio: 660,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Usina del Arte',
      'Planetario',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Av 9 de Julio'
    ],
    totalKm: 15
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc1'),
    nombre: 'Museum Tour',
    precio: 605,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Planetario',
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio'
    ],
    totalKm: 12
  }
]
```
IV)
```c
    db.recorridos.find({ stops: "San Telmo" } )
    -->
[
  {
    _id: ObjectId('682cc747bb118e581a521cb7'),
    nombre: 'Cultural Odyssey',
    precio: 715.0000000000001,
    stops: [
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio',
      'Plaza Italia'
    ],
    totalKm: 11
  },
  {
    _id: ObjectId('682cc747bb118e581a521cb9'),
    nombre: 'Tango Experience',
    precio: 385.00000000000006,
    stops: [
      'Avenida de Mayo',
      'Plaza del Congreso',
      'Paseo de la Historieta',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta'
    ],
    totalKm: 10
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbb'),
    nombre: 'Urban Exploration',
    precio: 495.00000000000006,
    stops: [
      'Avenida de Mayo',
      'Museo Moderno',
      'Paseo de la Historieta',
      'Usina del Arte',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Costanera Sur',
      'Plaza Italia'
    ],
    totalKm: 14,
    tags: [ 'Gastronomía' ]
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbc'),
    nombre: 'Gastronomic Delight',
    precio: 770.0000000000001,
    stops: [
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'Costanera Sur'
    ],
    totalKm: 9
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbe'),
    nombre: 'Architectural Expedition',
    precio: 550,
    stops: [
      'Usina del Arte',
      'Puerto Madero',
      'Museo Nacional de Bellas Artes',
      'Museo Moderno',
      'Museo de Arte Latinoamericano',
      'Teatro Colón',
      'San Telmo',
      'Recoleta'
    ],
    totalKm: 12
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbf'),
    nombre: 'Historic Landmarks',
    precio: 275,
    stops: [
      'Avenida de Mayo',
      'Plaza del Congreso',
      'Usina del Arte',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Costanera Sur',
      'Plaza Italia'
    ],
    totalKm: 23
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc0'),
    nombre: 'Artistic Journey',
    precio: 660,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Usina del Arte',
      'Planetario',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Av 9 de Julio'
    ],
    totalKm: 15
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc1'),
    nombre: 'Museum Tour',
    precio: 605,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Planetario',
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio'
    ],
    totalKm: 12
  }
]
```
V)
```c
    db.recorridos.find({ stops: "Recoleta" , stops: {$ne: "Plaza Italia"}} )
    -->
    [
  {
    _id: ObjectId('682cbf03ce69a625496c4bd0'),
    nombre: 'City Tour',
    precio: 220.00000000000003,
    stops: [ 'Diagonal Norte', 'Avenida deMayo', 'Plaza del Congreso' ],
    totalKm: 5
  },
  {
    _id: ObjectId('682cc747bb118e581a521cb5'),
    nombre: 'Nature Escape',
    precio: 440.00000000000006,
    stops: [ 'Delta', 'Río de la Plata', 'Av 9 de Julio', 'Puerto Madero' ]
  },
  {
    _id: ObjectId('682cc747bb118e581a521cb9'),
    nombre: 'Tango Experience',
    precio: 385.00000000000006,
    stops: [
      'Avenida de Mayo',
      'Plaza del Congreso',
      'Paseo de la Historieta',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta'
    ],
    totalKm: 10
  },
  {
    _id: ObjectId('682cc747bb118e581a521cba'),
    nombre: 'Delta Tour',
    precio: 880.0000000000001,
    stops: [ 'Río de la Plata', 'Bosques de Palermo', 'Delta', 'Tigre' ],
    totalKm: 8
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbc'),
    nombre: 'Gastronomic Delight',
    precio: 770.0000000000001,
    stops: [
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'Costanera Sur'
    ],
    totalKm: 9
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbd'),
    nombre: 'Riverfront Ramble',
    precio: 385.00000000000006,
    stops: [
      'Río de la Plata',
      'Bosques de Palermo',
      'El Monumental (Estadio River Plate)',
      'Costanera Sur'
    ],
    totalKm: 6
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbe'),
    nombre: 'Architectural Expedition',
    precio: 550,
    stops: [
      'Usina del Arte',
      'Puerto Madero',
      'Museo Nacional de Bellas Artes',
      'Museo Moderno',
      'Museo de Arte Latinoamericano',
      'Teatro Colón',
      'San Telmo',
      'Recoleta'
    ],
    totalKm: 12
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc0'),
    nombre: 'Artistic Journey',
    precio: 660,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Usina del Arte',
      'Planetario',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Av 9 de Julio'
    ],
    totalKm: 15
  },
  {
    _id: ObjectId('682cc747bb118e581a521cc1'),
    nombre: 'Museum Tour',
    precio: 605,
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Planetario',
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio'
    ],
    totalKm: 12
  }
]
```
VI)
```c
    db.recorridos.find({ stops: "Delta", precio: {$lt: 500} }, {nombre: 1, totalKm: 1, _id:0} )
    -->
    [ { nombre: 'Nature Escape' } ]
```
VII)
```c
    db.recorridos.find({ stops: {$all: ["San Telmo", "Recoleta", "Avenida de Mayo"] } } )
    -->
    [
  {
    _id: ObjectId('682cc747bb118e581a521cb9'),
    nombre: 'Tango Experience',
    precio: 385.00000000000006,
    stops: [
      'Avenida de Mayo',
      'Plaza del Congreso',
      'Paseo de la Historieta',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta'
    ],
    totalKm: 10
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbb'),
    nombre: 'Urban Exploration',
    precio: 495.00000000000006,
    stops: [
      'Avenida de Mayo',
      'Museo Moderno',
      'Paseo de la Historieta',
      'Usina del Arte',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Costanera Sur',
      'Plaza Italia'
    ],
    totalKm: 14,
    tags: [ 'Gastronomía' ]
  },
  {
    _id: ObjectId('682cc747bb118e581a521cbf'),
    nombre: 'Historic Landmarks',
    precio: 275,
    stops: [
      'Avenida de Mayo',
      'Plaza del Congreso',
      'Usina del Arte',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Costanera Sur',
      'Plaza Italia'
    ],
    totalKm: 23
  }
]
```
VIII)
```c
    db.recorridos.find({ $expr: {$gt: [{$size: "$stops"}, 5]} }, {nombre: 1, _id:0} )
    -->
[
  { nombre: 'City Lights' },
  { nombre: 'Cultural Odyssey' },
  { nombre: 'Historical Adventure' },
  { nombre: 'Tango Experience' },
  { nombre: 'Urban Exploration' },
  { nombre: 'Architectural Expedition' },
  { nombre: 'Historic Landmarks' },
  { nombre: 'Artistic Journey' },
  { nombre: 'Museum Tour' }
]
```
IX)
```c
    db.recorridos.find({ totalKm: {$exists: false}})
    -->
    [
  {
    _id: ObjectId('682cc747bb118e581a521cb5'),
    nombre: 'Nature Escape',
    precio: 440.00000000000006,
    stops: [ 'Delta', 'Río de la Plata', 'Av 9 de Julio', 'Puerto Madero' ]
  }
]
```
X)
```c
    db.recorridos.find({ stops: { $regex: "Museo" }}, {nombre: 1, stops: 1, _id:0} )
    -->
    [
  {
    nombre: 'City Lights',
    stops: [
      'Avenida de Mayo',
      'Paseo de la Historieta',
      'Usina del Arte',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Costanera Sur',
      'Plaza Italia',
      'Museo de Arte Latinoamericano'
    ]
  },
  {
    nombre: 'Urban Exploration',
    stops: [
      'Avenida de Mayo',
      'Museo Moderno',
      'Paseo de la Historieta',
      'Usina del Arte',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Costanera Sur',
      'Plaza Italia'
    ]
  },
  {
    nombre: 'Architectural Expedition',
    stops: [
      'Usina del Arte',
      'Puerto Madero',
      'Museo Nacional de Bellas Artes',
      'Museo Moderno',
      'Museo de Arte Latinoamericano',
      'Teatro Colón',
      'San Telmo',
      'Recoleta'
    ]
  },
  {
    nombre: 'Artistic Journey',
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Usina del Arte',
      'Planetario',
      'San Telmo',
      'La Boca - Caminito',
      'Belgrano - Barrio Chino',
      'Av 9 de Julio'
    ]
  },
  {
    nombre: 'Museum Tour',
    stops: [
      'Museo Nacional de Bellas Artes',
      'Teatro Colón',
      'Planetario',
      'Bosques de Palermo',
      'San Telmo',
      'La Boca - Caminito',
      'Recoleta',
      'El Monumental (Estadio River Plate)',
      'Av 9 de Julio'
    ]
  }
]
```
XI)
```c
    db.recorridos.countDocuments({})
    -->
    14
```