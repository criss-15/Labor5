package repository;

import java.util.ArrayList;
import java.util.List;

public abstract class InMemoryRepository<T> implements ICrudRepository<T> {

    protected List<T> repoList;

    public InMemoryRepository(){ this.repoList = new ArrayList();}

    @Override
    public void create(T obj){
        if(!repoList.contains(obj)){
            this.repoList.add(obj);
        }

    }

    @Override
    public List<T> getAll() {return this.repoList;}

    @Override
    public void delete(T obj) {
        if(repoList.contains(obj)){
            this.repoList.remove(obj);
        }
        System.out.println("The object is not in the list");
    }
}
