Seccion 5

1)SET agency "Cronos Tours"
OK

2)127.0.0.1:6379> TTL agency
(integer) -1

3)127.0.0.1:6379> EXPIRE agency 30
(integer) 1

4)127.0.0.1:6379> TTL agency
(integer) 5

5)127.0.0.1:6379> TTL agency
(integer) -2
127.0.0.1:6379> GET agency
(nil)

6)SETEX agency 20 "Cronos Tours"

/_
SET Crea o actualiza una clave con un valor.
TTL Muestra el tiempo de vida restante de una clave.
EXPIRE Establece una expiración en segundos para una clave.
GET Recupera el valor de una clave tipo string.
SETEX Crea una clave con valor y expiración en un solo paso.
_/

SECCION 6

1)RPUSH pets dog

2)127.0.0.1:6379> GET pets
(error) WRONGTYPE Operation against a key holding the wrong kind of value

127.0.0.1:6379> LRANGE pets 0 -1

1. "dog"

3)127.0.0.1:6379> LPUSH pets cat
(integer) 2

4)127.0.0.1:6379> RPUSH pets fish
(integer) 3

5)127.0.0.1:6379> TYPE pets
list

6)127.0.0.1:6379> LPOP pets
"cat"

7)127.0.0.1:6379> RPOP pets
"fish"

8)127.0.0.1:6379> RPUSH vuelo:ar389 aep mdz brc nqn mdq
(integer) 5

9)127.0.0.1:6379> SORT vuelo:ar389 ALPHA

1. "aep"
2. "brc"
3. "mdq"
4. "mdz"
5. "nqn"

   127.0.0.1:6379> LRANGE vuelo:ar389 0 -1

6. "aep"
7. "mdz"
8. "brc"
9. "nqn"
10. "mdq"

    10)127.0.0.1:6379> LINSERT vuelo:ar389 AFTER brc fte
    (integer) 6

    11)127.0.0.1:6379> LINSERT vuelo:ar389 BEFORE fte ush
    (integer) 7

    12)127.0.0.1:6379> LSET vuelo:ar389 -1 sla
    OK

    13)127.0.0.1:6379> LLEN vuelo:ar389
    (integer) 7

    14)127.0.0.1:6379> LINDEX vuelo:ar389 2
    "brc"

    15)127.0.0.1:6379> LREM vuelo:ar389 0 aep
    (integer) 1

    16)127.0.0.1:6379> LTRIM vuelo:ar389 3 5
    OK

    17)127.0.0.1:6379> RPUSH vuelo:ar389 fte
    (integer) 4

/_
RPUSH Inserta uno o más valores al final (derecha) de una lista.
LPUSH Inserta uno o más valores al inicio (izquierda) de una lista.
GET Recupera el valor de una clave tipo string. ❌ Da error si la clave es una lista.
LRANGE Obtiene los valores de una lista entre dos índices dados.
TYPE Devuelve el tipo de dato de una clave (list, string, set, etc.).
LPOP Elimina y retorna el primer elemento (izquierda) de una lista.
RPOP Elimina y retorna el último elemento (derecha) de una lista.
SORT Ordena los elementos de la lista (por defecto numéricamente).
SORT ... ALPHA Ordena los elementos alfabéticamente (necesario si los elementos son texto).
LINSERT Inserta un nuevo elemento antes o después de un valor dado.
LSET Modifica el valor de una posición específica de la lista.
LLEN Devuelve la cantidad de elementos en una lista.
LINDEX Devuelve el valor que está en una posición específica (índice).
LREM Elimina elementos que coinciden con un valor. Se puede limitar cuántos.
LTRIM Recorta la lista para que solo queden los elementos entre dos posiciones.
_/

SECCION 7

1)SADD airports eze aep nqn mdz mdq ush fte sla aep nqn brc cpc juj aep tuc eqs

2)127.0.0.1:6379> SCARD airports
(integer) 13

3)127.0.0.1:6379> SMEMBERS airports

1.  "eze"
2.  "aep"
3.  "nqn"
4.  "mdz"
5.  "mdq"
6.  "ush"
7.  "fte"
8.  "sla"
9.  "aep"
10. "nqn"
11. "brc"
12. "cpc"
13. "juj"
14. "aep"
15. "tuc"
16. "eqs"

4)127.0.0.1:6379> SREM airports cpc
(integer) 1

5)127.0.0.1:6379> SPOP airports
"eqs"

6)127.0.0.1:6379> SCARD airports
(integer) 11

7)127.0.0.1:6379> SISMEMBER airports cpc
(integer) 0

8)127.0.0.1:6379> SMOVE airports noa_airports sla
(integer) 1

127.0.0.1:6379> SMOVE airports noa_airports juj
(integer) 1

9)127.0.0.1:6379> SUNION airports noa_airports

1.  "mdq"
2.  "aep"
3.  "fte"
4.  "eze"
5.  "nqn"
6.  "mdz"
7.  "ush"
8.  "juj"
9.  "brc"
10. "tuc"
11. "sla"

10)127.0.0.1:6379> SUNIONSTORE total_airports airports noa_airports
(integer) 11

11)127.0.0.1:6379> SINTER total_airports noa_airports

1. "sla"
2. "juj"

   12)127.0.0.1:6379> SDIFF total_airports noa_airports

3. "brc"
4. "mdz"
5. "mdq"
6. "ush"
7. "aep"
8. "fte"
9. "eze"
10. "nqn"
11. "tuc"

/_
SADD Agrega uno o más elementos a un conjunto. Crea el conjunto si no existe.
SCARD Devuelve la cantidad de elementos únicos en un conjunto.
SMEMBERS Lista todos los elementos del conjunto (en orden aleatorio).
SREM Elimina uno o más elementos específicos de un conjunto.
SPOP Elimina y devuelve un elemento aleatorio del conjunto.
SISMEMBER Verifica si un valor existe dentro de un conjunto (retorna 1 o 0).
SMOVE Mueve un elemento de un conjunto a otro.
SUNION Devuelve la unión (todos los elementos únicos) de dos o más conjuntos.
SUNIONSTORE Guarda la unión de conjuntos en un nuevo conjunto destino.
SINTER Devuelve la intersección (elementos en común) de dos o más conjuntos.
SDIFF Devuelve los elementos que están en el primer conjunto pero no en los demás.
_/
