package mx.unam.ciencias.icc;

import java.util.Random;
/**
 * <p>Clase para matrices de 2×2.</p>
 *
 * <p>Las matrices de 2×2 pueden sumarse, multiplicarse y sacar su
 * determinante.</p>
 *
 * <p>Las matrices se crean con cuatro dobles a, b, c y d, tales que representan
 * a la matriz:</p>
 *
<pre>
 ⎛ a  b ⎞
 ⎝ c  d ⎠
</pre>
 */
public class Matriz2x2 {

    /* La primera entrada de la matriz. */
    private double a;
    /* La segunda entrada de la matriz. */
    private double b;
    /* La tercera entrada de la matriz. */
    private double c;
    /* La cuarta entrada de la matriz. */
    private double d;

    /**
     * Constructor único. Dado que no proveemos <em>setters</em>, nuestras
     * matrices de 2×2 son <em>inmutables</em>; no podemos cambiar sus valores.
     * @param a la primera entrada de la matriz.
     * @param b la segunda entrada de la matriz.
     * @param c la tercera entrada de la matriz.
     * @param d la cuarta entrada de la matriz.
     */
    public Matriz2x2(double a, double b,
                     double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
       
        // Aquí va su código.
    }

    /**
     * Regresa el elemento <tt>a</tt> de la matriz de 2×2.
     * @return El elemento <tt>a</tt> de la matriz de 2×2.
     */
    public double getA() {
        return a;
    }

    /**
     * Regresa el elemento <tt>b</tt> de la matriz de 2×2.
     * @return El elemento <tt>b</tt> de la matriz de 2×2.
     */
    public double getB() {
    	return b;
        // Aquí va su código.
    }

    /**
     * Regresa el elemento <tt>c</tt> de la matriz de 2×2.
     * @return El elemento <tt>c</tt> de la matriz de 2×2.
     */
    public double getC() {
    	return c;
        // Aquí va su código.
    }

    /**
     * Regresa el elemento <tt>d</tt> de la matriz de 2×2.
     * @return El elemento <tt>d</tt> de la matriz de 2×2.
     */
    public double getD() {
    	return d;
        // Aquí va su código.
    }

    /**
     * Suma la matriz de 2×2 con la matriz de 2×2 que recibe como parámetro.
     * @param m La matriz de 2×2 con la que hay que sumar.
     * @return La suma con la matriz de 2×2 <tt>m</tt>.
     */
    public Matriz2x2 suma(Matriz2x2 m) {
    	double n00;
    	double n01;
    	double n10;
    	double n11;
    	
    	n00= m.getA();
    	n01= m.getB();
    	n10= m.getC();
    	n11= m.getD();
    	
    	Matriz2x2 r = new 
    	Matriz2x2(a+n00,b+n01,c+n10,d+n11);
    	
    	return r;
    	
    	
    			
    			
    			
    }
        // Aquí va su código.
    

    /**
     * Multiplica la matriz de 2×2 con la matriz de 2×2 que recibe como
     * parámetro.
     * @param m La matriz de 2×2 con la que hay que multiplicar.
     * @return La multiplicación con la matriz de 2×2 <tt>m</tt>.
     */
    public Matriz2x2 multiplica(Matriz2x2 m) {
    	double n00;
    	double n01;
    	double n10;
    	double n11;
    	
    	n00= m.getA();
    	n01= m.getB();
    	n10= m.getC();
    	n11= m.getD();
    	
    	Matriz2x2 r = new
    	Matriz2x2(a*n00+b*n10,a*n01+b*n11,c*n00+d*n10,c*n01+d*n11);
    	return r;
    	
        // Aquí va su código.
    }

    /**
     * Multiplica la matriz de 2×2 con la constante que recibe como parámetro.
     * @param x La constante con la que hay que multiplicar.
     * @return La multiplicación con la constante <tt>x</tt>.
     */
    public Matriz2x2 multiplica(double x) {
        
    	
    	Matriz2x2 r = new
    	Matriz2x2(x*a,x*b,x*c,x*d);
    	return r;
    	
        // Aquí va su código.
    }

    /**
     * Calcula el determinante de la matriz de 2×2.
     * @return El determinante de la matriz de 2×2.
     */
    public double determinante() {
    	double r;
    			r = a*d-b*c;
    			return r;
    
        // Aquí va su código.
    }
	 /**
     * Calcula la inversa de la matriz de 2×2.
     *
     * Si multiplicamos una matriz de 2×2 con su inversa, obtenemos la matriz
     * identidad.
     * @return La inversa de la matriz de 2×2, o <tt>null</tt> si la matriz no
     *         es inversible.
     */
    public Matriz2x2 inversa() {
	double det=determinante();
	if(det==0)
	throw new IllegalStateException();
    if(determinante()!=0){
    	Matriz2x2 r= new Matriz2x2 (d/determinante(),(-b)/determinante(),(-c)/determinante(),a/	determinante());
     return r;}
    else
    	return null;
    	
        // Aquí va su código.
    }

    /**
     * Calcula la <em>n</em>-ésima potencia de la matriz de 2×2.
     *
     * La <em>n</em>-ésima potencia de una matriz de 2×2 es el resultado de
     * multiplicar la matriz consigo misma <em>n</em> veces.
     * @param n La potencia a la que hay que elevar la matriz; si <em>n</em> es
     *          menor que 2, regresa una copia de la matriz de 2×2.
     * @return la <em>n</em>-ésima potencia de la matriz de 2×2.
     */
    public Matriz2x2 potencia(int n) {
    	
    	if(n<2){
    		Matriz2x2 r1=  new Matriz2x2(a,b,c,d);
    		return r1;
     	}else {
    		
    		
     	        double ra = a;
    	        double rb = b;
    	        double rc = c;
    	        double rd = d;
    	        int i = 1;
    	        while (i < n) {
    	            double t1 = ra * a + rb * c;
    	            double t2 = ra * b + rb * d;
    	            double t3 = rc * a + rd * c;
    	            double t4 = rc * b + rd * d;
    	            ra = t1;
    	            rb = t2;
    	            rc = t3;
    	            rd = t4;
    	            i++;
    	        }
    			
    	        Matriz2x2 s=  new Matriz2x2(ra,rb,rc,rd);
    		
    		return s;
    	}
        // Aqí va su código.
    }

    /* Agrega espacios a la cadena hasta que tenga longitud n. */
    private String agregaEspacios(String s, int n) {
        String r = s;
        while (r.length() < n)
            r = " " + r;
        return r;
    }
    
    /**
     * Regresa una cadena con la representación de la matriz.
     * @return una cadena con la representación de la matriz.
     */
    public String toString() {

        

        String sa = String.format("%2.3f", a);
        String sb = String.format("%2.3f", b);
        String sc = String.format("%2.3f", c);
        String sd = String.format("%2.3f", d);

        int n = Math.max(Math.max(sa.length(), sb.length()),
                         Math.max(sc.length(), sd.length()));

        sa = agregaEspacios(sa, n);
        sb = agregaEspacios(sb, n);
        sc = agregaEspacios(sc, n);
        sd = agregaEspacios(sd, n);

        String s =
            String.format("⎛ %s, %s ⎞\n", sa, sb) +
            String.format("⎝ %s, %s ⎠",   sc, sd);
        
        return s;
    		
        }

	@Override public boolean equals(Object o) {
		if (o instanceof Matriz2x2) {
			Matriz2x2 m = (Matriz2x2)o;
			if(a==m.a&&b==m.b&&c==m.c&&d==m.d)
				return true;
			return false;
		} return false;
	}
    	
        // Aquí va su código.
    }

