import { ProductListPipe } from './product-list.pipe';

describe('ProductListPipe', () => {
  it('create an instance', () => {
    const pipe = new ProductListPipe();
    expect(pipe).toBeTruthy();
  });
});
