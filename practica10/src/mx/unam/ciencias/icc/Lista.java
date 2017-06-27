package mx.unam.ciencias.icc;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Iterable<T>{

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
	public T elemento;
        public Nodo anterior;
        public Nodo siguiente;

        public Nodo(T elemento) {
	this.elemento=elemento;
           
        }
        // Aquí va su código.
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        public Lista<T>.Nodo anterior;
        public Lista<T>.Nodo siguiente;

        public Iterador() {
	anterior=null;
	siguiente=cabeza;

	
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
	if (siguiente == null)
	return false;
	return true;
            // Aquí va su código.
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
	if(siguiente==null)
	throw new  NoSuchElementException();
	
	anterior=siguiente;
	siguiente=siguiente.siguiente;
	return anterior.elemento;
	
            // Aquí va su código.
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
	if (anterior == null)
	return false;
	return true;
            // Aquí va su código.
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
	if(anterior==null)
	throw new NoSuchElementException();
	
	siguiente=anterior;
	anterior=anterior.anterior;
	return siguiente.elemento;
	
            // Aquí va su código.
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
	anterior = null;
	siguiente = cabeza;
            // Aquí va su código.
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
	anterior = rabo;
	siguiente = null;
            // Aquí va su código.
        }

        /* No implementamos este método. */
        @Override public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
	return longitud;
        // Aquí va su código.
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
	if(cabeza==null && rabo==null){
	return true;
	}else
	return false;

        // Aquí va su código.
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
	if(elemento==null)
		throw new IllegalArgumentException();
	Nodo nuevo = new Nodo(elemento);
        if (cabeza == null && rabo == null){
        	cabeza = nuevo;
        	rabo = nuevo;
        	longitud = longitud + 1;
        }else{
        	rabo.siguiente=nuevo;
        	nuevo.anterior = rabo;
        	rabo=nuevo;
        	longitud = longitud + 1;
        }
        // Aquí va su código.
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último. Después de llamar este
     * método, el iterador apunta a la cabeza de la lista.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public Nodo buscaNodo(T elemento) {
	    
	Nodo n=cabeza;
	   
	while(n !=null)
	   
	    {
		if( n.elemento.equals(elemento))
		    return n;
		n=n.siguiente;
		  
	    }
		 
		 
	return null;
	 
   
    }
	public void agregaInicio(T elemento) {
	if(elemento==null)
		throw new IllegalArgumentException();
	Nodo nuevo = new Nodo(elemento);
        if (cabeza == null && rabo == null){
        	cabeza = nuevo;
        	rabo = nuevo;
        	longitud = longitud + 1;
        }else{
           	cabeza.anterior=nuevo;
        	nuevo.siguiente = cabeza;
        	if (longitud==1)
        		rabo=cabeza;
        	cabeza=nuevo;
        	longitud = longitud + 1;
        }
	
        // Aquí va su código.
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica. Si un elemento de la lista es
     * modificado, el iterador se mueve al primer elemento.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
	
	if(cabeza==null&&rabo==null) 
	    return ;
        
    
	Nodo n= buscaNodo(elemento);
	
	
	if(cabeza.equals(rabo) && n.elemento.equals(elemento)){
	cabeza=null;
	rabo=null;
	longitud--;
	}else if(n.equals(cabeza)&&(!n.equals(rabo))){
	cabeza=cabeza.siguiente;
	cabeza.anterior=null;
	longitud--;
	}else if(n.equals(rabo)&&(!n.equals(cabeza))){
	rabo=rabo.anterior;
	rabo.siguiente=null;
	rabo.siguiente=null;
	longitud--;
	} else{
	n.siguiente.anterior=n.anterior;
	n.anterior.siguiente=n.siguiente;
	longitud--;
	}
	
	}

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
	if(rabo==null && cabeza==null)
        	throw new NoSuchElementException();
      if(rabo.equals(cabeza)) {
	     longitud--;
	     T s=cabeza.elemento;
	     cabeza=rabo=null;
	     return s; }
	    
	    else{ 
     	T s= cabeza.elemento;
    	cabeza=cabeza.siguiente;
    	cabeza.anterior=null;
    	longitud--;
    	return s;
}
        // Aquí va su código.
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
	if(rabo==null && cabeza==null)
        	throw new NoSuchElementException ();
      if(rabo.equals(cabeza)) {
	     longitud--;
	     T s=cabeza.elemento;
	     cabeza=rabo=null;
	     return s; }
	    
	    else{ 
     	T s= rabo.elemento;
    	rabo=rabo.anterior;
    	rabo.siguiente=null;
    	longitud--;
    	return s;
}
        // Aquí va su código.
    }

    /**
     * Nos dice si un elemento está en la lista. El iterador no se mueve.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(T elemento) {
	if(!esVacia()){
    		Nodo n= cabeza;
    		while(n!=null){
     			if(n.elemento.equals(elemento)){
     				return true;
    				}
    			n=n.siguiente;
    		}
     		 return false;
    	}else
    		return false;
        // Aquí va su código.
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
	Nodo n=cabeza;
    	Lista<T> r = new Lista<T>();
    	while(n!=null){
        r.agregaInicio(n.elemento);
    	n=n.siguiente;
    	}
    	return r;
        // Aquí va su código.
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
	Lista<T> r= new Lista<T>();
        Nodo t=cabeza;
	while (t !=null)
	    {
		r.agregaFinal(t.elemento);
		t=t.siguiente;
			 
	    }
	return r;
        // Aquí va su código.
    }

    /**
     * Limpia la lista de elementos. El llamar este método es equivalente a
     * eliminar todos los elementos de la lista.
     */
    public void limpia() {
	cabeza=rabo=null;
		longitud=0;
        // Aquí va su código.
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
	 if(esVacia()){
	   throw new NoSuchElementException();
   	}else {
	   T n;
	   n=cabeza.elemento;
			   return n;
		   
   } 
	
        // Aquí va su código.
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
	if(esVacia()){
	throw new NoSuchElementException();
   }else{
	 T n;
     n=rabo.elemento;
    return n;		 
        
    }
        // Aquí va su código.
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
	if(i<0)
        	throw new ExcepcionIndiceInvalido();
        if (i>=longitud)
        	throw new ExcepcionIndiceInvalido();
        Nodo n=cabeza;
	int con=0;
	while(n !=null){
	if(con==i)
	return n.elemento;
	n=n.siguiente;
	con++;
	}
	return null;
        // Aquí va su código.
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
	if(!esVacia()){
        	Nodo n=cabeza;
        	int i=0;
        	while(n!=null){
        		T aux = n.elemento;
        		if (elemento.equals(aux)){
        			return i;
        		}
        		n=n.siguiente;
        		i++;
        	}

        	return -1;
        }else{
        		return -1;
    	}
        // Aquí va su código.
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
	String s = "[";

        Nodo n= cabeza;
        while(n!=null){

        	if(n.siguiente==null){
        		s += String.format("%s", n.elemento);
        	}else{
        		s += String.format("%s, ", n.elemento);
		}
        	n=n.siguiente;
        }
        s += "]";
        return s;
        // Aquí va su código.
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
	if (o == null)
            return false;
        if (!(o instanceof Lista))
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
	  Nodo f=cabeza;
	 Nodo g=lista.cabeza;
	if(longitud==0 && lista.longitud==0)
return true;
	if(longitud==lista.longitud)
	{
  			while(f!=null){
				if(f.elemento.equals(g.elemento)){
					f=f.siguiente;
					g=g.siguiente;
					
					}else {
					return false;
					}
				if(f==rabo&&f.elemento.equals(g.elemento))
					return true;
 
	}  


	}return false;
        // Aquí va su código.
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

/**
 * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
 * tiene que contener nada más elementos que implementan la interfaz {@link
 * Comparable}.
 * @param <T> tipo del que puede ser la lista.
 * @param lista la lista que se ordenará.
 * @return una copia de la lista recibida, pero ordenada.
 */
public static <T extends Comparable<T>>
Lista<T> mergeSort(Lista<T> l) {

	if (l.longitud == 0 || l.longitud == 1)
		return l.copia();
	Lista<T> ini = new Lista<T>();
	Lista<T> fin = new Lista<T>();
	Lista<T>.Nodo la = l.cabeza;
	for(int i=0 ; i < l.longitud / 2 ; i++) {
		ini.agregaFinal(la.elemento);
		la = la.siguiente;
	}
	for (int i = l.longitud / 2 ; i < l.longitud ; i++) {
		fin.agregaFinal(la.elemento);
		la = la.siguiente;
	}
	ini = Lista.mergeSort(ini);
	fin = Lista.mergeSort(fin);
	return mezclador(ini,fin);
}

private static <T extends Comparable<T>>
Lista<T> mezclador(Lista<T> ini, Lista<T> fin){

	Lista<T>.Nodo listaIni = ini.cabeza;
	Lista<T>.Nodo listaFin = fin.cabeza;
	Lista<T> nueva = new Lista<T>();

	while(listaIni != null && listaFin != null){
		if(listaIni.elemento.compareTo(listaFin.elemento) < 0){
		nueva.agregaFinal(listaIni.elemento);
		listaIni = listaIni.siguiente;
		}
		else{
		nueva.agregaFinal(listaFin.elemento);
		listaFin = listaFin.siguiente;
		}
	}
	while(listaIni != null){
		nueva.agregaFinal(listaIni.elemento);
		listaIni = listaIni.siguiente;
	}
	while(listaFin != null){
		nueva.agregaFinal(listaFin.elemento);
		listaFin = listaFin.siguiente;
	}
	return nueva;
}

/**
 * Busca un elemento en una lista ordenada. La lista recibida tiene que
 * contener nada más elementos que implementan la interfaz {@link
 * Comparable}, y se da por hecho que está ordenada.
 * @param <T> tipo del que puede ser la lista.
 * @param l la lista donde se buscará.
 * @param e el elemento a buscar.
 * @return <tt>true</tt> si e está contenido en la lista,
 *         <tt>false</tt> en otro caso.
 */
public static <T extends Comparable<T>>
boolean busquedaLineal(Lista<T> l, T e) {
return l.contiene(e)!=false;
}
}


