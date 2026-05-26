package guru.springframework.spring7restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring7restmvc.ex.NotFoundException;
import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

	private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<Beer> listBeers(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<Beer> getBeerById(UUID id) {

        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        
//        if(!beerMap.containsKey(id)) {
//        	log.error("Beer with id " + id.toString() + " not found. Cannot get.");
//        	throw new NotFoundException("Beer with id " + id.toString() + " not found. Cannot get.");
//        }
        
        //return beerMap.get(id);
        return Optional.ofNullable(beerMap.get(id));
    }

	@Override
	public Beer saveNewBeer(Beer beer) {
		// TODO Auto-generated method stub
		UUID randomUUID = UUID.randomUUID();
		
		Beer newBeer = Beer.builder()
				.id(randomUUID)
				.version(1)
				.beerName(beer.getBeerName())
				.beerStyle(beer.getBeerStyle())
				.upc(beer.getUpc())
				.price(beer.getPrice())
				.quantityOnHand(beer.getQuantityOnHand())
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();
		
		beerMap.put(randomUUID, newBeer);
		
		return newBeer;
	}

	@Override
	public Beer updateBeerById(UUID id, Beer beer) {
		// TODO Auto-generated method stub
		
		if(id == null) {
			log.debug("Beer id is null. Cannot update.");
			throw new RuntimeException("Beer id is null. Cannot update.");
		}
		if(beer == null) {
			log.debug("Beer is null. Cannot update.");
			throw new RuntimeException("Beer is null. Cannot update.");
		}
		if(!id.equals(beer.getId())) {
			log.debug("Beer id does not match with the id in the path. Cannot update.");
			throw new RuntimeException("Beer id does not match with the id in the path. Cannot update.");
		}
		
		if(beerMap.containsKey(id)) {
			Beer existingBeer = beerMap.get(id);
			
			existingBeer.setVersion(existingBeer.getVersion() + 1);
			existingBeer.setBeerName(beer.getBeerName());
			existingBeer.setBeerStyle(beer.getBeerStyle());
			existingBeer.setUpc(beer.getUpc());
			existingBeer.setPrice(beer.getPrice());
			existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
			existingBeer.setUpdateDate(LocalDateTime.now());
			
			//beerMap.put(id, existingBeer);
			
			return existingBeer;
		} else {
			log.debug("Beer with id " + id.toString() + " not found. Cannot update.");
			throw new RuntimeException("Beer with id " + id.toString() + " not found. Cannot update.");
		}
	}

	@Override
	public void deleteBeerById(UUID id) {
		// TODO Auto-generated method stub
		if(id == null) {
			log.debug("Beer id is null. Cannot delete.");
			throw new RuntimeException("Beer id is null. Cannot delete.");
		}
		if(beerMap.containsKey(id)) {
			beerMap.remove(id);
		} else {
			log.debug("Beer with id " + id.toString() + " not found. Cannot delete.");
			//throw new RuntimeException("Beer with id " + id.toString() + " not found. Cannot delete.");
		}
	}

	@Override
	public Beer patchBeerById(UUID id, Beer beer) {
		// TODO Auto-generated method stub
		if(id == null) {
			log.debug("Beer id is null. Cannot patch.");
			throw new RuntimeException("Beer id is null. Cannot patch.");
		}
		if(beer == null) {
			log.debug("Beer is null. Cannot patch.");
			throw new RuntimeException("Beer is null. Cannot patch.");
		}
		if(!id.equals(beer.getId())) {
			log.debug("Beer id does not match with the id in the path. Cannot patch.");
			throw new RuntimeException("Beer id does not match with the id in the path. Cannot patch.");
		}
		if(beerMap.containsKey(id)) {
			Beer existingBeer = beerMap.get(id);
			
			if(beer.getBeerName() != null) {
				existingBeer.setBeerName(beer.getBeerName());
			}
			if(beer.getBeerStyle() != null) {
				existingBeer.setBeerStyle(beer.getBeerStyle());
			}
			if(beer.getUpc() != null) {
				existingBeer.setUpc(beer.getUpc());
			}
			if(beer.getPrice() != null) {
				existingBeer.setPrice(beer.getPrice());
			}
			if(beer.getQuantityOnHand() != null) {
				existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
			}
			
			existingBeer.setVersion(existingBeer.getVersion() + 1);
			existingBeer.setUpdateDate(LocalDateTime.now());
			
			//beerMap.put(id, existingBeer);
			
			return existingBeer;
		} else {
			log.debug("Beer with id " + id.toString() + " not found. Cannot patch.");
			throw new RuntimeException("Beer with id " + id.toString() + " not found. Cannot patch.");
		}
	}
}