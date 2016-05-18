-- select
select * from "public".usuario;
select * from "public".usuarioempresa;

select * from "public".empresa;
select * from "public".filialempresa;

-- clean
delete from "public".usuarioempresa;
delete from "public".usuario;
delete from "public".filialempresa;
delete from "public".empresa;

        EntityManager em = GestorEntityManagerProvider.getEntityManager();

        try {

            
            em.getTransaction().begin();

            em.getTransaction().commit();
            
            
        } catch (Exception ex) {
            em.getTransaction().rollback();
            Logger.getLogger(DashboardModel.class.getName()).log(Level.SEVERE, null, ex);
        }
