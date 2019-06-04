package com.project.services;

import com.project.entities.Product;
import com.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class InsertSampleDataService {
	@Autowired
	private UserService usersService;
	
	@Autowired
	private RolesService rolesService;
	
	@PostConstruct
		public void init() {
			User admin= new User("admin@email.com","admin","admin");
			admin.setPassword("admin");
			admin.setRole(rolesService.getRoles()[1]);
			usersService.addUser(admin);


			//Usuario 2
			User user1= new User("javi@email.com","Javi","Garcia");
			user1.setPassword("123456");
			user1.setRole(rolesService.getRoles()[0]);
			Set user1Products = new HashSet<Product>() {
				{
					add(new Product("Gafas de sol","Buenas gafas con proteccion", 14,user1));
					add(new Product("Sofa","Sofa para sentarse", 100,user1));
					add(new Product("Boligrafo","Para escribir", 1,user1));
				}
			};
			user1.setProducts(user1Products);

			usersService.addUser(user1);


			//Usuario 2
			User user2= new User("ivan@email.com","Ivan","Prieto");
			user2.setPassword("123456");
			user2.setRole(rolesService.getRoles()[0]);


			Set user2Products = new HashSet<Product>() {
				{
					add(new Product("Opel corsa","Coche del año 2005", 1400,user2));
					add(new Product("Cuadro","Cuadro de Pikaso", 150,user2));
					add(new Product("Mechero","Para quemar cosas", 1,user2));
				}
			};
			user2.setProducts(user2Products);

			usersService.addUser(user2);



			for (int i=0;i<3;i++){
				User user= new User("user"+i+1+"2@email.com","User"+i+1,"Normal user");
				user.setPassword("123456");
				user.setRole(rolesService.getRoles()[0]);
				usersService.addUser(user);
				Set userProducts= new HashSet<Product>();
				for (int j=0;j<3;j++){
					userProducts.add(new Product("Producto:"+i+""+j,"Descripcion"+i+""+j,Math.random()*1000,user));

				}

				user.setProducts(userProducts);
				usersService.addUser(user);
			}


		}
}