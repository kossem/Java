import java.util.Scanner;

public class MapRoute {
    static class Vertex {
        public int index, way,a, b, dist;
        public Vertex parent;
        public Vertex() {
            index = way = a = b = dist = 0;
        }
    }
    public static Vertex ExtractMin(PQ a) {
        Vertex ret = a.array[0];
        a.top--;
        if(a.top != 0) {
            a.array[0] = a.array[a.top];
            a.array[0].index = 0;
            Heapify(a, a.array);
        }
        else if (a.top < 0)
            return ret;
        return ret;
    }
    public static void Insert(PQ a, Vertex v) {
        int i = a.top;
        a.top++;
        a.array[i] = v;
        while(i != 0 && a.array[(i-1)/2].dist > a.array[i].dist) {
            Vertex elem = a.array[(i-1)/2];
            a.array[(i-1)/2] = a.array[i];
            a.array[i] = elem;
            a.array[i].index = i;
            i = (i-1)/2;
        }
        a.array[i].index = i;
    }
    public static void DecreaseKey(PQ a, int key, int v ) {
        Vertex t;
        a.array[v].dist = key;
        while(v != 0 && a.array[(v-1)/2].dist > key) {
            t = a.array[(v-1)/2];
            a.array[(v-1)/2] = a.array[v];
            a.array[v] = t;
            a.array[v].index = v;
            v = (v-1)/2;
        }
        a.array[v].index = v;
    }
    public static void Heapify(PQ a, Vertex[] array) {
        int i = 0, j, left, right;
        Vertex t;
        while(true){
            left = 2*i + 1;
            right = left + 1;
            j = i;
            if(left < a.top)
                if(array[left].dist < array[i].dist)
                    i = left;
            if(right < a.top)
                if(array[right].dist < array[i].dist)
                    i = right;
            if(i == j)
                break;
            else if(i!=j){
                //Swap(array[i],array[j]);
                t = array[i];
                array[i] = array[j];
                array[j] = t;
                array[i].index = i;
                array[j].index = j;
            }
        }
    }
    public static void Swap (Vertex a, Vertex b){
        Vertex t;
        t = a;
        a = b;
        b = t;
    }
    public static boolean Relax(Vertex u, Vertex v, int w) {
        boolean changed = (u.dist + w < v.dist);
        if(changed) {
            v.dist = u.dist + w;
            v.parent = u;
        }
        return  changed;
    }
    public static void Dijkstra(Vertex[][] Graph, int n) {
        PQ a = new PQ(n*n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Vertex v = Graph[i][j];
                if(v == Graph[0][0]) { v.dist = v.way; }
                else { v.dist =  (int)Double.POSITIVE_INFINITY; }
                v.parent = null;
                Insert(a,v);
            }
        }
        while(a.top != 0) {
            Vertex v = ExtractMin(a);
            v.index = -1;
            if((v.a > 0)&&(Graph[v.a -1][v.b].index != -1))
                if(Relax(v, Graph[v.a -1][v.b], Graph[v.a -1][v.b].way)) {
                    DecreaseKey(a, Graph[v.a -1][v.b].dist, Graph[v.a -1][v.b].index);
                }
            if((v.a + 1 < n)&&(Graph[v.a +1][v.b].index != -1))
                if(Relax(v,Graph[v.a +1][v.b],Graph[v.a +1][v.b].way)){
                    DecreaseKey(a, Graph[v.a +1][v.b].dist, Graph[v.a +1][v.b].index);
                }
            if((v.b + 1 < n)&&(Graph[v.a][v.b +1].index != -1))
                if(Relax(v,Graph[v.a][v.b +1],Graph[v.a][v.b +1].way)){
                    DecreaseKey(a, Graph[v.a][v.b +1].dist, Graph[v.a][v.b +1].index);
                }
            if((v.b > 0)&&(Graph[v.a][v.b -1].index != -1))
                if(Relax(v,Graph[v.a][v.b -1], Graph[v.a][v.b -1].way)) {
                    DecreaseKey(a, Graph[v.a][v.b -1].dist, Graph[v.a][v.b -1].index);
                }
        }
    }
    static class PQ {
        private int top;
        private  Vertex[] array;
        PQ(int n) {
            top = 0;
            array = new Vertex[n];
            for (int i = 0; i < n; i++)
                array[i] = new Vertex();
        }
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Vertex[][] Graph = new Vertex[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                Graph[i][j] = new Vertex();
        for(int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                Graph[i][j].parent = null;
                Graph[i][j].way = in.nextInt();
                Graph[i][j].a = i;
                Graph[i][j].b = j;
            }
        if(n !=0 ) {
            Dijkstra(Graph, n);
            System.out.println(Graph[n - 1][n - 1].dist);
        }
        else System.out.println(0);
    }
}
