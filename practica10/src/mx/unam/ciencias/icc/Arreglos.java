package mx.unam.ciencias.icc;

/**
 * Clase para ordenar y buscar en arreglos genéricos.
 */
public class Arreglos {
	private static <T extends Comparable<T>>
    void intercambia(T[] a, int i, int j) {
		T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> el tipo del arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>>
    void selectionSort(T[] a) {
	int i, j, m;
    for(i = 0; i < a.length - 1; i++){
        m = i;
          for(j= i + 1; j < a.length; j++)
                if(a[j].compareTo(a[m])==-1)
                    m = j;
             intercambia(a, i, m);

      }
        // Aquí va su código.
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> el tipo del arreglo.
     * @param a un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>>
    void quickSort(T[] a) {
	quickSort(a, 0, (a.length - 1));

     }

     private static <T extends Comparable<T>>
                      void quickSort(T[] a, int ini, int fin) {
         if((fin - ini)<1)
             return;
         int i = ini + 1;
         int j = fin;
         while(i < j)
             if(a[i].compareTo(a[ini]) > 0 && a[j].compareTo(a[ini]) <= 0)
                 intercambia(a,i++,j--);
             else if(a[i].compareTo(a[ini]) <= 0)
                 i++;
             else
                 j--;

         if(a[i].compareTo(a[ini]) > 0)
             i--;

         intercambia(a,i,ini);
         quickSort(a, ini,i-1);
         quickSort(a,i+1, fin);
        // Aquí va su código.
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa
     * el índice del elemento en el arreglo, o -1 si no se
     * encuentra.
     * @param <T> el tipo del arreglo.
     * @param a el arreglo dónde buscar.
     * @param e el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se
     * encuentra.
     */
    public static <T extends Comparable<T>>
    int busquedaBinaria(T[] a, T e) {
	return busquedaBinaria(a, e, 0,a.length-1);

  }

  private static <T extends Comparable<T>>
  int busquedaBinaria(T[] a, T e, int ini, int fin){
      
      if(fin < ini)
         return -1;
      else{
         int mitad = (fin + ini)/2;
          if(e.compareTo(a[mitad]) > 0)
              return busquedaBinaria(a, e, mitad +1 , fin);
          else if(e.compareTo(a[mitad]) < 0)
              return busquedaBinaria(a, e, ini, mitad-1);
          else
              return mitad;
    }
  }
        // Aquí va su código.
    }

